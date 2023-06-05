package com.team.bpm.presentation.ui.studio_detail

import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.model.StudioDetailTabType


interface StudioDetailContract : BaseContract<StudioDetailContract.State, StudioDetailContract.Event, StudioDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val isReviewListLoading: Boolean = false,
        val studio: Studio? = null,
        val userId: Long? = null,
        val originalReviewList: List<Review> = emptyList(),
        val reviewList: List<Review> = emptyList(),
        val focusedTab: StudioDetailTabType = StudioDetailTabType.INFO,
        val isReviewListShowingImageReviewsOnly: Boolean = false,
        val isReviewListSortedByLike: Boolean = true,
        val isTopRecommendListExpanded: Boolean = false,
        val selectedReview: Review? = null,
        val bottomSheetButton: BottomSheetButton? = null,
        val isBottomSheetShowing: Boolean = false,
        val isReportDialogShowing: Boolean = false,
        val isNoticeDialogShowing: Boolean = false,
        val noticeDialogContent: String? = null
    )

    sealed interface Event {
        object GetUserId : Event

        object GetStudioDetail : Event

        object GetReviewList : Event

        object OnErrorOccurred : Event

        object OnClickQuit : Event

        object OnClickInfoTab : Event

        object OnClickReviewTab : Event

        object OnScrolledAtInfoArea : Event

        object OnScrolledAtReviewArea : Event

        data class OnClickCall(val number: String) : Event

        data class OnClickCopyAddress(val address: String) : Event

        data class OnClickNavigate(val address: String) : Event

        object OnMissingNavigationApp : Event

        object OnClickEditInfoSuggestion : Event

        object OnClickWriteReview : Event

        object OnClickMoreReviews : Event

        object OnClickShowImageReviewsOnly : Event

        object OnClickShowNotOnlyImageReviews : Event

        object OnClickSortByLike : Event

        object OnClickSortByDate : Event

        object OnClickExpandTopRecommendList : Event

        object OnClickCollapseTopRecommendList : Event

        data class OnClickReviewLikeButton(val reviewId: Int) : Event

        object OnClickScrap : Event

        data class OnClickReviewActionButton(val review: Review) : Event

        object OnClickDismissReportDialog : Event

        object OnClickDismissNoticeDialog : Event

        object OnClickDeleteReview : Event

        object OnClickReportReview : Event

        data class OnClickSendReviewReport(val reason: String) : Event

        object OnClickBackButton : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object LoadFailed : Effect

        object Quit : Effect

        object ScrollToInfoTab : Effect

        object ScrollToReviewTab : Effect

        data class Call(val number: String) : Effect

        data class CopyAddressToClipboard(val address: String) : Effect

        data class LaunchNavigationApp(val address: String) : Effect

        object GoToRegisterStudio : Effect

        data class GoToWriteReview(val studioId: Int) : Effect

        data class GoToReviewList(val studioId: Int) : Effect

        object ExpandBottomSheet : Effect

        object CollapseBottomSheet : Effect

        object RefreshReviewList : Effect
    }
}