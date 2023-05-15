package com.team.bpm.presentation.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Studio
import com.team.bpm.domain.usecase.schedule.GetScheduleUseCase
import com.team.bpm.presentation.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val saveScheduleUseCase: GetScheduleUseCase
) : ViewModel(), ScheduleContract {
    private val _state = MutableStateFlow(ScheduleContract.State())
    override val state: StateFlow<ScheduleContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ScheduleContract.Effect>()
    override val effect: SharedFlow<ScheduleContract.Effect> = _effect.asSharedFlow()

    override fun event(event: ScheduleContract.Event) = when (event) {
        is ScheduleContract.Event.OnClickEdit -> {
            onClickEdit()
        }

        is ScheduleContract.Event.OnClickSearchStudio -> {
            onClickSearchStudio()
        }

        is ScheduleContract.Event.SetStudio -> {
            setStudio(event.studio)
        }

        is ScheduleContract.Event.OnClickDate -> {
            onClickDate(event.date)
        }

        is ScheduleContract.Event.OnClickSetTime -> {
            onClickSetTime(event.time)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun setStudio(studio: Studio) {
        viewModelScope.launch {
            _state.update {
                it.copy(selectedStudio = studio)
            }
        }
    }

    private fun onClickEdit() {
        _state.update {
            it.copy(isEditing = true)
        }
    }

    private fun onClickSearchStudio() {
        viewModelScope.launch {
            _effect.emit(ScheduleContract.Effect.GoToSelectStudio)
        }
    }

    private fun onClickDate(date: LocalDate) {
        _state.update {
            it.copy(selectedDate = date)
        }
    }

    private fun onClickSetTime(time: String) {
        _state.update {
            it.copy(selectedTime = time)
        }
    }
}

//@HiltViewModel
//class ScheduleViewModel @Inject constructor(
//    private val getScheduleUseCase: GetScheduleUseCase,
//    private val saveScheduleUseCase: SaveScheduleUseCase,
//    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
//    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
//) : BaseViewModel() {
//    private val _event = MutableSharedFlow<ScheduleViewEvent>()
//    val event: SharedFlow<ScheduleViewEvent>
//        get() = _event
//
//    private val _state = MutableStateFlow<ScheduleState>(ScheduleState.Init)
//    val state: StateFlow<ScheduleState>
//        get() = _state
//
//    private val exceptionHandler: CoroutineExceptionHandler by lazy {
//        CoroutineExceptionHandler { coroutineContext, throwable ->
//
//        }
//    }
//
//    init {
//        getSchedule()
//    }
//
//    fun onClickSave() {
//        viewModelScope.launch(mainDispatcher) {
//            _event.emit(ScheduleViewEvent.Save)
//        }
//    }
//
//    private fun getSchedule() {
//        viewModelScope.launch(mainDispatcher) {
//            _state.emit(ScheduleState.Loading)
//        }
//
//        viewModelScope.launch(ioDispatcher + exceptionHandler) {
//            getScheduleUseCase().onEach { state ->
//                when (state) {
//                    is ResponseState.Success -> _state.emit(ScheduleState.GetSuccess(state.data))
//                    is ResponseState.Error -> _state.emit(ScheduleState.Error)
//                }
//            }.launchIn(viewModelScope)
//        }
//    }
//
//    fun saveSchedule(
//        studioName: String,
//        date: String,
//        time: String,
//        memo: String
//    ) {
//        viewModelScope.launch(mainDispatcher) {
//            _state.emit(ScheduleState.Loading)
//        }
//
//        viewModelScope.launch(ioDispatcher + exceptionHandler) {
//            saveScheduleUseCase(
//                studioName = studioName,
//                date = date,
//                time = modifyTimeToFormat(time),
//                memo = memo
//            ).onEach { state ->
//                when (state) {
//                    is ResponseState.Success -> _state.emit(ScheduleState.SaveSuccess(state.data))
//                    is ResponseState.Error -> _state.emit(ScheduleState.Error)
//                }
//            }.launchIn(viewModelScope)
//        }
//    }
//
//    private fun modifyTimeToFormat(
//        time: String
//    ): String {
//        val timeInList = time.dropLast(5).split(":")
//        return "${if (time.contains("오후")) timeInList[0].toInt() + 12 else timeInList[0]}:${timeInList[1]}:00"
//    }
//}