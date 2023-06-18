package com.team.bpm.presentation.ui.main.studio.schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.schedule.EditScheduleUseCase
import com.team.bpm.domain.usecase.schedule.GetScheduleUseCase
import com.team.bpm.domain.usecase.schedule.MakeScheduleUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val makeScheduleUseCase: MakeScheduleUseCase,
    private val editScheduleUseCase: EditScheduleUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModelV2(), ScheduleContract {
    private val _state = MutableStateFlow(ScheduleContract.State())
    override val state: StateFlow<ScheduleContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ScheduleContract.Effect>()
    override val effect: SharedFlow<ScheduleContract.Effect> = _effect.asSharedFlow()

    override fun event(event: ScheduleContract.Event) = when (event) {
        is ScheduleContract.Event.GetSchedule -> {
            getSchedule()
        }

        is ScheduleContract.Event.OnClickEdit -> {
            onClickEdit()
        }

        is ScheduleContract.Event.OnClickSearchStudio -> {
            onClickSearchStudio()
        }

        is ScheduleContract.Event.SetStudio -> {
            setStudio(event.studioName)
        }

        is ScheduleContract.Event.OnClickDate -> {
            onClickDate(event.date)
        }

        is ScheduleContract.Event.OnClickSetTime -> {
            onClickSetTime(event.time)
        }

        is ScheduleContract.Event.OnClickSubmit -> {
            onClickSubmit(event.scheduleName, event.memo)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getScheduleId(): Int? {
        return savedStateHandle.get<Int>(ScheduleActivity.KEY_SCHEDULE_ID)
    }

    private fun getSchedule() {
        getScheduleId()?.let { scheduleId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        isLoading = true,
                        scheduleId = scheduleId
                    )
                }

                withContext(ioDispatcher) {
                    getScheduleUseCase(scheduleId).onEach { schedule ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    fetchedScheduleName = schedule.scheduleName,
                                    selectedDate = LocalDate.parse(schedule.date, DateTimeFormatter.ISO_LOCAL_DATE),
                                    selectedTime = schedule.time.toString(),
                                    selectedStudioName = schedule.studioName,
                                    fetchedMemo = schedule.memo
                                )
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        } ?: run {
            viewModelScope.launch {
                _state.update {
                    it.copy(isEditing = true)
                }
            }
        }
    }

    private fun setStudio(studioName: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(selectedStudioName = studioName)
            }
        }
    }

    private fun onClickEdit() {
        viewModelScope.launch {
            _state.update {
                it.copy(isEditing = true)
            }
        }
    }

    private fun onClickSearchStudio() {
        viewModelScope.launch {
            _effect.emit(ScheduleContract.Effect.GoToSelectStudio)
        }
    }

    private fun onClickDate(date: LocalDate) {
        viewModelScope.launch {
            _state.update {
                it.copy(selectedDate = date)
            }
        }
    }

    private fun onClickSetTime(time: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(selectedTime = time)
            }
        }
    }

    private fun onClickSubmit(
        scheduleName: String,
        memo: String
    ) {
        state.value.selectedDate?.let { selectedDate ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    if (state.value.scheduleId == null) {
                        makeScheduleUseCase(
                            scheduleName = scheduleName,
                            studioName = state.value.selectedStudioName ?: "",
                            date = selectedDate.toString(),
                            time = state.value.selectedTime,
                            memo = memo
                        )
                    } else {
                        editScheduleUseCase(
                            scheduleId = state.value.scheduleId!!,
                            scheduleName = scheduleName,
                            studioName = state.value.selectedStudioName ?: "",
                            date = selectedDate.toString(),
                            time = state.value.selectedTime,
                            memo = memo
                        )
                    }.onEach { schedule ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                scheduleId = schedule.id,
                                isEditing = false,
                                fetchedScheduleName = schedule.scheduleName,
                                selectedDate = selectedDate,
                                selectedTime = schedule.time.toString(),
                                selectedStudioName = schedule.studioName,
                                fetchedMemo = schedule.memo
                            )
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }
}
