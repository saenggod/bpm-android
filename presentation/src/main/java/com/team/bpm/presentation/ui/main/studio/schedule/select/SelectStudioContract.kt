package com.team.bpm.presentation.ui.main.studio.schedule.select

import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.base.BaseContract

interface SelectStudioContract : BaseContract<SelectStudioContract.State, SelectStudioContract.Event, SelectStudioContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val studioList: List<Studio> = emptyList(),
        val studioCount: Int = 0,
        val selectedStudio: Studio? = null
    )

    sealed interface Event {
        data class OnClickSearch(val query: String) : Event
        data class OnClickStudio(val studio: Studio) : Event
        object OnClickComplete : Event
    }

    sealed interface Effect {
        data class Finish(val studioName: String) : Effect
    }
}