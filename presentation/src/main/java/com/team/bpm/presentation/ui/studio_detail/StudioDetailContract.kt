package com.team.bpm.presentation.ui.studio_detail

import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.StudioDetailTabType


interface StudioDetailContract : BaseContract<StudioDetailContract.State, StudioDetailContract.Event, StudioDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val studio: Studio? = null,
        val reviewList: List<Review>? = null,
        val isErrorDialogShowing: Boolean = false,
        val focusedTab: StudioDetailTabType = StudioDetailTabType.Info
    )

    sealed interface Event {
        object GetStudioDetailData : Event
        object ShowErrorDialog : Event
        object OnClickQuit : Event
        object OnClickInfoTab : Event
        object OnClickReviewTab : Event
        object OnScrolledAtInfoArea : Event
        object OnScrolledAtReviewArea : Event
    }

    sealed interface Effect {
        object LoadFailed : Effect
        object Quit : Effect
        object ScrollToInfoTab : Effect
        object ScrollToReviewTab : Effect
    }
}