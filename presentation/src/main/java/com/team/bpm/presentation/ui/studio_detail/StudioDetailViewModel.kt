package com.team.bpm.presentation.ui.studio_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.usecase.review.*
import com.team.bpm.domain.usecase.studio.GetStudioDetailUseCase
import com.team.bpm.domain.usecase.studio.ScrapCancelUseCase
import com.team.bpm.domain.usecase.studio.ScrapUseCase
import com.team.bpm.domain.usecase.user.GetUserIdUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.model.StudioDetailTabType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StudioDetailViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getStudioDetailUseCase: GetStudioDetailUseCase,
    private val scrapUseCase: ScrapUseCase,
    private val scrapCancelUseCase: ScrapCancelUseCase,
    private val getReviewListUseCase: GetReviewListUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val reportReviewUseCase: ReportReviewUseCase,
    private val likeReviewUseCase: LikeReviewUseCase,
    private val dislikeReviewUseCase: DislikeReviewUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModelV2(), StudioDetailContract {

    private val _state = MutableStateFlow(StudioDetailContract.State())
    override val state: StateFlow<StudioDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<StudioDetailContract.Effect>()
    override val effect: SharedFlow<StudioDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: StudioDetailContract.Event) = when (event) {
        is StudioDetailContract.Event.GetUserId -> {
            getUserId()
        }

        is StudioDetailContract.Event.GetStudioDetail -> {
            getStudioDetail()
        }

        is StudioDetailContract.Event.OnClickInfoTab -> {
            onClickInfoTab()
        }

        is StudioDetailContract.Event.OnScrolledAtInfoArea -> {
            onScrolledAtInfoArea()
        }

        is StudioDetailContract.Event.OnScrolledAtReviewArea -> {
            onScrolledAtReviewArea()
        }

        is StudioDetailContract.Event.OnClickCall -> {
            onClickCall(event.number)
        }

        is StudioDetailContract.Event.OnClickCopyAddress -> {
            onClickCopyAddress(event.address)
        }

        is StudioDetailContract.Event.OnClickNavigate -> {
            onClickNavigate(event.address)
        }

        is StudioDetailContract.Event.OnMissingNavigationApp -> {
            onMissingNavigationApp()
        }

        is StudioDetailContract.Event.OnClickExpandTopRecommendList -> {
            onClickExpandTopRecommendList()
        }

        is StudioDetailContract.Event.OnClickCollapseTopRecommendList -> {
            onClickCollapseTopRecommendList()
        }

        is StudioDetailContract.Event.OnClickScrap -> {
            onClickScrap()
        }

        is StudioDetailContract.Event.OnClickReviewTab -> {
            onClickReviewTab()
        }

        is StudioDetailContract.Event.OnClickWriteReview -> {
            onClickWriteReview()
        }

        is StudioDetailContract.Event.GetReviewList -> {
            getReviewList()
        }

        is StudioDetailContract.Event.OnClickMoreReviews -> {
            onClickMoreReviews()
        }

        is StudioDetailContract.Event.OnClickShowImageReviewsOnly -> {
            onClickShowImageReviewsOnly()
        }

        is StudioDetailContract.Event.OnClickShowNotOnlyImageReviews -> {
            onClickShowNotOnlyImageReviews()
        }

        is StudioDetailContract.Event.OnClickSortByLike -> {
            onClickSortByLike()
        }

        is StudioDetailContract.Event.OnClickSortByDate -> {
            onClickSortByDate()
        }

        is StudioDetailContract.Event.OnClickReviewActionButton -> {
            onClickReviewActionButton(event.review)
        }

        is StudioDetailContract.Event.OnClickDeleteReview -> {
            onClickDeleteReview()
        }

        is StudioDetailContract.Event.OnClickReportReview -> {
            onClickReportReview()
        }

        is StudioDetailContract.Event.OnClickSendReviewReport -> {
            onClickSendReviewReport(event.reason)
        }

        is StudioDetailContract.Event.OnClickReviewLikeButton -> {
            onClickReviewLikeButton(event.reviewId)
        }

        is StudioDetailContract.Event.OnClickDismissReportDialog -> {
            onClickDismissReportDialog()
        }

        is StudioDetailContract.Event.OnClickDismissNoticeDialog -> {
            onClickDismissNoticeDialog()
        }

        is StudioDetailContract.Event.OnBottomSheetHide -> {
            onBottomSheetHide()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getUserId() {
        viewModelScope.launch(ioDispatcher) {
            getUserIdUseCase().onEach { result ->
                result?.let { userId ->
                    withContext(mainImmediateDispatcher) {
                        _state.update {
                            it.copy(userId = userId)
                        }
                    }
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    private fun getStudioId(): Int? {
        return savedStateHandle.get<Int>(StudioDetailActivity.KEY_STUDIO_ID)
    }

    private fun getStudioDetail() {
        getStudioId()?.let { studioId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    getStudioDetailUseCase(studioId).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    studio = result
                                )
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }


    private fun onClickInfoTab() {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.ScrollToInfoTab)
        }
    }

    private fun onScrolledAtInfoArea() {
        viewModelScope.launch {
            _state.update {
                it.copy(focusedTab = StudioDetailTabType.INFO)
            }
        }
    }

    private fun onScrolledAtReviewArea() {
        viewModelScope.launch {
            _state.update {
                it.copy(focusedTab = StudioDetailTabType.REVIEW)
            }
        }
    }

    private fun onClickCall(number: String) {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.Call(number))
        }
    }

    private fun onClickCopyAddress(address: String) {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.CopyAddressToClipboard(address))
        }
    }

    private fun onClickNavigate(address: String) {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.LaunchNavigationApp(address))
        }
    }

    private fun onMissingNavigationApp() {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.ShowToast("지도 앱이 설치되어 있지 않습니다."))
        }
    }

    private fun onClickExpandTopRecommendList() {
        viewModelScope.launch {
            _state.update {
                it.copy(isTopRecommendListExpanded = true)
            }
        }
    }

    private fun onClickCollapseTopRecommendList() {
        _state.update {
            it.copy(isTopRecommendListExpanded = false)
        }
    }

    private fun onClickScrap() {
        state.value.studio?.id?.let { studioId ->
            viewModelScope.launch(ioDispatcher) {
                when (state.value.studio?.scrapped) {
                    true -> {
                        scrapCancelUseCase(studioId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        studio = it.studio?.copy(
                                            scrapCount = it.studio.scrapCount?.minus(1),
                                            scrapped = false
                                        )
                                    )
                                }
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }

                    false -> {
                        scrapUseCase(studioId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        studio = it.studio?.copy(
                                            scrapCount = it.studio.scrapCount?.plus(1),
                                            scrapped = true
                                        )
                                    )
                                }
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }

                    null -> {
                        _effect.emit(StudioDetailContract.Effect.ShowToast("스크랩 기능을 사용할 수 없습니다."))
                    }
                }
            }
        }
    }

    private fun onClickReviewTab() {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.ScrollToReviewTab)
        }
    }

    private fun onClickWriteReview() {
        state.value.studio?.id?.let { studioId ->
            viewModelScope.launch {
                _effect.emit(StudioDetailContract.Effect.GoToWriteReview(studioId))
            }
        }
    }

    private fun getReviewList() {
        getStudioId()?.let { studioId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isReviewListLoading = true)
                }

                getReviewListUseCase(studioId).onEach { result ->
                    withContext(ioDispatcher) {
                        _state.update {
                            it.copy(
                                isReviewListLoading = false,
                                reviewList = result.reviews?.filter { it.reported == false } ?: emptyList()
                            )
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
            }
        }
    }


    private fun onClickMoreReviews() {
        state.value.studio?.id?.let { studioId ->
            viewModelScope.launch {
                _effect.emit(StudioDetailContract.Effect.GoToReviewList(studioId))
            }
        }
    }

    private fun onClickShowImageReviewsOnly() {
        viewModelScope.launch {
            _state.update {
                it.copy(isReviewListShowingImageReviewsOnly = true)
            }
        }
    }

    private fun onClickShowNotOnlyImageReviews() {
        viewModelScope.launch {
            _state.update {
                it.copy(isReviewListShowingImageReviewsOnly = false)
            }
        }
    }

    private fun onClickSortByLike() {
        viewModelScope.launch {
            _state.update {
                it.copy(isReviewListSortedByLike = true)
            }
        }
    }

    private fun onClickSortByDate() {
        viewModelScope.launch {
            _state.update {
                it.copy(isReviewListSortedByLike = false)
            }
        }
    }


    private fun sortRefreshedReviewList(list: List<Review>): List<Review> {
        val filteredList = if (state.value.isReviewListShowingImageReviewsOnly) {
            list.filter { it.filesPath?.isNotEmpty() == true }
        } else {
            list
        }

        return if (state.value.isReviewListSortedByLike) {
            filteredList.sortedByDescending { it.likeCount }
        } else {
            filteredList.sortedByDescending { it.createdAt }
        }
    }

    private fun onClickReviewActionButton(review: Review) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    bottomSheetButton = if (review.author?.id == it.userId) BottomSheetButton.DELETE_POST else BottomSheetButton.REPORT_POST,
                    isBottomSheetShowing = true,
                    selectedReview = review
                )
            }
        }
    }

    private fun onClickDeleteReview() {
        getStudioId()?.let { studioId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        isLoading = true,
                        isBottomSheetShowing = false
                    )
                }

                state.value.selectedReview?.id?.let { reviewId ->
                    withContext(ioDispatcher) {
                        deleteReviewUseCase(studioId, reviewId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        isNoticeDialogShowing = true,
                                        noticeDialogContent = "삭제가 완료되었습니다."
                                    )
                                }

                                _effect.emit(StudioDetailContract.Effect.RefreshReviewList)
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }
                } ?: run {
                    _state.update {
                        it.copy(isLoading = false)
                    }

                    _effect.emit(StudioDetailContract.Effect.ShowToast("리뷰를 삭제할 수 없습니다."))
                }
            }
        }
    }

    private fun onClickReportReview() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isReportDialogShowing = true,
                    isBottomSheetShowing = false
                )
            }
        }
    }

    private fun onClickSendReviewReport(reason: String) {
        getStudioId()?.let { studioId ->
            state.value.selectedReview?.id?.let { reviewId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isReviewListLoading = true,
                            isReportDialogShowing = false,
                            isNoticeDialogShowing = true,
                            noticeDialogContent = "신고가 완료되었습니다."
                        )
                    }

                    withContext(ioDispatcher) {
                        reportReviewUseCase(studioId, reviewId, reason).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(isReviewListLoading = false)
                                }

                                _effect.emit(StudioDetailContract.Effect.RefreshReviewList)
                                _effect.emit(StudioDetailContract.Effect.ScrollToReviewTab)
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }
                }
            }
        }
    }

    private fun onClickReviewLikeButton(reviewId: Int) {
        state.value.reviewList.find { review -> review.id == reviewId }?.let { review ->
            viewModelScope.launch(ioDispatcher) {
                when (review.liked) {
                    true -> {
                        state.value.studio?.id?.let { studioId ->
                            dislikeReviewUseCase(studioId, reviewId).onEach {
                                withContext(mainImmediateDispatcher) {
                                    _state.update {
                                        it.copy(reviewList = sortRefreshedReviewList(it.reviewList.toMutableList().apply {
                                            val targetIndex = indexOf(find { review -> review.id == reviewId })
                                            this[targetIndex] = this[targetIndex].copy(
                                                liked = false,
                                                likeCount = this[targetIndex].likeCount?.minus(1)
                                            )
                                        }))
                                    }
                                }
                            }.launchIn(viewModelScope + exceptionHandler)
                        }
                    }

                    false -> {
                        state.value.studio?.id?.let { studioId ->
                            likeReviewUseCase(studioId, reviewId).onEach {
                                withContext(mainImmediateDispatcher) {
                                    _state.update {
                                        it.copy(reviewList = sortRefreshedReviewList(it.reviewList.toMutableList().apply {
                                            val targetIndex = indexOf(find { review -> review.id == reviewId })
                                            this[targetIndex] = this[targetIndex].copy(
                                                liked = true,
                                                likeCount = this[targetIndex].likeCount?.plus(1)
                                            )
                                        }))
                                    }
                                }
                            }.launchIn(viewModelScope + exceptionHandler)
                        }
                    }

                    null -> {
                        withContext(mainImmediateDispatcher) {
                            _effect.emit(StudioDetailContract.Effect.ShowToast("좋아요 기능을 사용할 수 없습니다."))
                        }
                    }
                }
            }
        }
    }


    private fun onClickDismissReportDialog() {
        viewModelScope.launch {
            _state.update {
                it.copy(isReportDialogShowing = false)
            }
        }
    }

    private fun
            onClickDismissNoticeDialog() {
        viewModelScope.launch {
            _state.update {
                it.copy(isNoticeDialogShowing = false)
            }
        }
    }

    private fun onBottomSheetHide() {
        viewModelScope.launch {
            _state.update {
                it.copy(isBottomSheetShowing = false)
            }
        }
    }
}