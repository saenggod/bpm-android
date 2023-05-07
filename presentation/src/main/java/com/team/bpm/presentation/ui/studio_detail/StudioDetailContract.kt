package com.team.bpm.presentation.ui.studio_detail

import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.base.BaseContract


interface StudioDetailContract : BaseContract<StudioDetailContract.State, StudioDetailContract.Event, StudioDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val studio: Studio? = null,
    )

    sealed interface Event {
        object GetStudioDetailData : Event
    }

    sealed interface Effect {
        data class ShowToast(val message: String) : Effect
    }
}