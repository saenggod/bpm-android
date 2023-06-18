package com.team.bpm.presentation.ui.main.studio.schedule

import com.team.bpm.presentation.base.BaseContract
import java.time.LocalDate

interface ScheduleContract : BaseContract<ScheduleContract.State, ScheduleContract.Event, ScheduleContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val scheduleId: Int? = null,
        val isEditing: Boolean = false,
        val fetchedScheduleName: String? = null,
        val selectedDate: LocalDate? = null,
        val selectedTime: String? = null,
        val selectedStudioName: String? = null,
        val fetchedMemo: String? = null
    )

    sealed interface Event {
        object GetSchedule : Event

        object OnClickEdit : Event

        object OnClickSearchStudio : Event

        data class SetStudio(val studioName: String) : Event

        data class OnClickDate(val date: LocalDate) : Event

        data class OnClickSetTime(val time: String) : Event

        data class OnClickSubmit(
            val scheduleName: String,
            val memo: String
        ) : Event
    }

    sealed interface Effect {

        object GoToSelectStudio : Effect
    }
}