package com.team.bpm.presentation.ui.main.studio.detail

import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.model.StudioDetailTabType


interface StudioDetailContract : BaseContract<StudioDetailContract.State, StudioDetailContract.Event, StudioDetailContract.Effect> {
    data class State(
        val userId: Long? = null,
        val focusedTab: StudioDetailTabType = StudioDetailTabType.INFO,
        val isTopRecommendListExpanded: Boolean = false,
        val isLoading: Boolean = false,
        val studio: Studio? = null,
        val isReviewListLoading: Boolean = false,
        val reviewList: List<Review> = emptyList(),
        val isReviewListShowingImageReviewsOnly: Boolean = false,
        val isReviewListSortedByLike: Boolean = true,
        val selectedReview: Review? = null,
        val isBottomSheetShowing: Boolean = false,
        val bottomSheetButton: BottomSheetButton? = null,
        val isReportDialogShowing: Boolean = false,
        val isNoticeDialogShowing: Boolean = false,
        val noticeDialogContent: String = ""
    )

    sealed interface Event {
        object GetUserId : Event

        object GetStudioDetail : Event

        object OnClickInfoTab : Event

        object OnScrolledAtInfoArea : Event

        object OnScrolledAtReviewArea : Event

        data class OnClickCall(val number: String) : Event

        data class OnClickCopyAddress(val address: String) : Event

        data class OnClickNavigate(val address: String) : Event

        object OnMissingNavigationApp : Event

        object OnClickExpandTopRecommendList : Event

        object OnClickCollapseTopRecommendList : Event

        object OnClickScrap : Event

        object OnClickReviewTab : Event

        object OnClickWriteReview : Event

        object GetReviewList : Event

        object OnClickMoreReviews : Event

        object OnClickShowImageReviewsOnly : Event

        object OnClickShowNotOnlyImageReviews : Event

        object OnClickSortByLike : Event

        object OnClickSortByDate : Event

        data class OnClickReviewActionButton(val review: Review) : Event

        object OnClickDeleteReview : Event

        object OnClickReportReview : Event

        data class OnClickSendReviewReport(val reason: String) : Event

        object OnClickDismissReportDialog : Event

        object OnClickDismissNoticeDialog : Event

        data class OnClickReviewLikeButton(val reviewId: Int) : Event

        object OnBottomSheetHide : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object Quit : Effect

        object ScrollToInfoTab : Effect

        object ScrollToReviewTab : Effect

        data class Call(val number: String) : Effect

        data class CopyAddressToClipboard(val address: String) : Effect

        data class LaunchNavigationApp(val address: String) : Effect

        object RefreshStudioDetail : Effect

        object RefreshReviewList : Effect

        data class GoToWriteReview(val studioId: Int) : Effect

        data class GoToReviewList(val studioId: Int) : Effect
    }
}