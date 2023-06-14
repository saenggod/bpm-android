package com.team.bpm.presentation.ui.studio_detail.review_list

import com.team.bpm.domain.model.Review
import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.BottomSheetButton

interface ReviewListContract : BaseContract<ReviewListContract.State, ReviewListContract.Event, ReviewListContract.Effect> {
    data class State(
        val userId: Long? = null,
        val isLoading: Boolean = false,
        val reviewList: List<Review> = emptyList(),
        val isReviewListShowingImageReviewsOnly: Boolean = false,
        val isReviewListSortedByLike: Boolean = true,
        val selectedReview: Review? = null,
        val selectedReviewIndex: Int = 0,
        val isBottomSheetShowing: Boolean = false,
        val bottomSheetButton: BottomSheetButton? = null,
        val isReportDialogShowing: Boolean = false,
        val isNoticeDialogShowing: Boolean = false,
        val noticeDialogContent: String = "",
    )

    sealed interface Event {
        object GetUserId : Event

        data class OnClickReviewActionButton(val review: Review, val index: Int) : Event

        object OnClickWriteReview : Event

        object OnClickDeleteReview : Event

        object OnClickReportReview : Event

        data class OnClickSendReviewReport(val reason: String) : Event

        data class OnClickReviewLike(val reviewId: Int) : Event

        object GetReviewList : Event

        object OnClickShowImageReviewsOnly : Event

        object OnClickShowNotOnlyImageReviews : Event

        object OnClickSortByLike : Event

        object OnClickSortByDate : Event

        object OnClickDismissReportDialog : Event

        object OnClickDismissNoticeDialog : Event

        object OnBottomSheetHide : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object RefreshReviewList : Effect

        data class GoToWriteReview(val studioId: Int) : Effect
    }
}