package com.team.bpm.presentation.ui.schedule

import com.team.bpm.presentation.base.UnidirectionalViewModel

interface ScheduleContract : UnidirectionalViewModel<ScheduleContract.State, ScheduleContract.Event, ScheduleContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val isEditing: Boolean = false,
    )

    sealed interface Event {
        object OnClickEdit : Event
        object OnClickSearchStudio : Event
    }

    sealed interface Effect {
        object GoToSelectStudio : Effect
    }
}