package com.team.bpm.presentation.ui.studio_detail

import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.base.BaseContract


interface StudioDetailContract : BaseContract<StudioDetailContract.State, StudioDetailContract.Event, StudioDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val studio: Studio? = null,
        val reviewList: List<Review>? = null,
        val isErrorDialogShowing: Boolean = false
    )

    sealed interface Event {
        object GetStudioDetailData : Event
        object ShowErrorDialog : Event
        object OnClickQuit : Event
    }

    sealed interface Effect {
        object LoadFailed : Effect
        object Quit : Effect
    }
}