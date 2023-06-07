package com.team.bpm.presentation.ui.studio_detail.review_list

import com.team.bpm.domain.model.Review
import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.BottomSheetButton

interface ReviewListContract : BaseContract<ReviewListContract.State, ReviewListContract.Event, ReviewListContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val userId: Long? = null,
        val originalReviewList: List<Review> = emptyList(),
        val reviewList: List<Review> = emptyList(),
        val isReviewListShowingImageReviewsOnly: Boolean = false,
        val isReviewListSortedByLike: Boolean = true,
        val selectedReview: Review? = null,
        val isBottomSheetShowing: Boolean? = null,
        val isNoticeDialogShowing: Boolean = false,
        val isReportDialogShowing: Boolean = false,
        val noticeDialogContent: String? = null,
        val bottomSheetButton: BottomSheetButton? = null
    )

    sealed interface Event {
        object GetUserId : Event

        object GetReviewList : Event

        object OnClickShowImageReviewsOnly : Event

        object OnClickShowNotOnlyImageReviews : Event

        object OnClickSortByLike : Event

        object OnClickSortByDate : Event

        object OnClickWriteReview : Event

        data class OnClickReviewLikeButton(val reviewId: Int) : Event

        data class OnClickReviewActionButton(val review: Review) : Event

        object OnClickDeleteReview : Event

        object OnClickReportReview : Event

        data class OnClickSendReviewReport(val reason: String) : Event

        object OnClickDismissNoticeDialog : Event

        object OnClickDismissReportDialog : Event

        object OnClickBackButton : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object RefreshReviewList : Effect

        data class GoToWriteReview(val studioId: Int) : Effect
    }
}