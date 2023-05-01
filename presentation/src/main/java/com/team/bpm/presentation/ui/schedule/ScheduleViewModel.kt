package com.team.bpm.presentation.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.schedule.GetScheduleUseCase
import com.team.bpm.presentation.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val saveScheduleUseCase: GetScheduleUseCase,
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
    }

    private fun onClickEdit() {
        _state.update {
            it.copy(isEditing = !_state.value.isEditing)
        }
    }

    private fun onClickSearchStudio() {
        viewModelScope.launch {
            _effect.emit(ScheduleContract.Effect.GoToSelectStudio)
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