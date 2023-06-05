package com.team.bpm.presentation.ui.studio_detail.review_detail

import com.team.bpm.domain.model.Review
import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.BottomSheetButton

interface ReviewDetailContract : BaseContract<ReviewDetailContract.State, ReviewDetailContract.Event, ReviewDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val userId: Long? = null,
        val review: Review? = null,
        val liked: Boolean? = false,
        val likeCount: Int? = null,
        val isBottomSheetShowing: Boolean? = null,
        val bottomSheetButtonList: List<BottomSheetButton> = emptyList(),
        val isNoticeDialogShowing: Boolean = false,
        val isReportDialogShowing: Boolean = false
    )

    sealed interface Event {
        object GetUserId : Event

        object GetReviewDetail : Event

        object OnClickLike : Event

        object OnClickReviewActionButton : Event

        object OnClickDeleteReview : Event

        object OnClickReportReview : Event

        data class OnClickSendReviewReport(val reason: String) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object GoBack : Effect
    }
}