package com.team.bpm.presentation.ui.schedule

import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.base.BaseContract
import java.time.LocalDate

interface ScheduleContract : BaseContract<ScheduleContract.State, ScheduleContract.Event, ScheduleContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val isEditing: Boolean = false,
        val selectedDate: LocalDate? = null,
        val selectedTime: String? = null,
        val selectedStudio: Studio? = null
    )

    sealed interface Event {
        object OnClickEdit : Event
        object OnClickSearchStudio : Event
        data class SetStudio(val studio: Studio) : Event
        data class OnClickDate(val date: LocalDate) : Event
        data class OnClickSetTime(val time: String) : Event
    }

    sealed interface Effect {
        object GoToSelectStudio : Effect
    }
}