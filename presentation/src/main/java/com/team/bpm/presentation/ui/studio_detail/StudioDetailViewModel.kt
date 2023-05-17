package com.team.bpm.presentation.ui.studio_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.usecase.review.GetReviewListUseCase
import com.team.bpm.domain.usecase.review.like.DislikeReviewUseCase
import com.team.bpm.domain.usecase.review.like.LikeReviewUseCase
import com.team.bpm.domain.usecase.scrap.ScrapCancelUseCase
import com.team.bpm.domain.usecase.scrap.ScrapUseCase
import com.team.bpm.domain.usecase.studio_detail.StudioDetailUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import com.team.bpm.presentation.model.StudioDetailTabType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StudioDetailViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val studioDetailUseCase: StudioDetailUseCase,
    private val reviewListUseCase: GetReviewListUseCase,
    private val likeReviewUseCase: LikeReviewUseCase,
    private val dislikeReviewUseCase: DislikeReviewUseCase,
    private val scrapUseCase: ScrapUseCase,
    private val scrapCancelUseCase: ScrapCancelUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), StudioDetailContract {

    private val _state = MutableStateFlow(StudioDetailContract.State())
    override val state: StateFlow<StudioDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<StudioDetailContract.Effect>()
    override val effect: SharedFlow<StudioDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: StudioDetailContract.Event) = when (event) {
        is StudioDetailContract.Event.GetStudioDetail -> {
            getStudioDetail()
        }

        is StudioDetailContract.Event.GetReviewList -> {
            getReviewList()
        }

        is StudioDetailContract.Event.OnErrorOccurred -> {
            showErrorDialog()
        }

        is StudioDetailContract.Event.OnClickQuit -> {
            onClickQuit()
        }

        is StudioDetailContract.Event.OnClickInfoTab -> {
            onClickInfoTab()
        }

        is StudioDetailContract.Event.OnClickReviewTab -> {
            onClickReviewTab()
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

        is StudioDetailContract.Event.OnClickEditInfoSuggestion -> {
            onClickEditInfoSuggestion()
        }

        is StudioDetailContract.Event.OnClickWriteReview -> {
            onClickWriteReview()
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

        is StudioDetailContract.Event.OnClickExpandTopRecommendList -> {
            onClickExpandTopRecommendList()
        }

        is StudioDetailContract.Event.OnClickCollapseTopRecommendList -> {
            onClickCollapseTopRecommendList()
        }

        is StudioDetailContract.Event.OnClickReviewLikeButton -> {
            onClickReviewLikeButton(event.reviewId)
        }

        is StudioDetailContract.Event.OnClickScrap -> {
            onClickScrap()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

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

                withContext(ioDispatcher + exceptionHandler) {
                    studioDetailUseCase(studioId).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            when (result) {
                                is ResponseState.Success -> {
                                    _state.update {
                                        it.copy(isLoading = false, studio = result.data)
                                    }
                                }

                                is ResponseState.Error -> {
                                    _state.update {
                                        it.copy(isLoading = false)
                                    }

                                    // TODO : Show error dialog
                                }
                            }
                        }
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    private fun getReviewList() {
        getStudioId()?.let { studioId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isReviewLoading = true)
                }

                reviewListUseCase(studioId).onEach { result ->
                    withContext(ioDispatcher + exceptionHandler) {
                        when (result) {
                            is ResponseState.Success -> {
                                result.data.reviews?.let { reviews ->
                                    _state.update {
                                        it.copy(isReviewLoading = false, originalReviewList = reviews, reviewList = sortRefreshedReviewList(reviews))
                                    }
                                }
                            }

                            is ResponseState.Error -> {
                                _state.update {
                                    it.copy(isReviewLoading = false)
                                }

                                // TODO : Show error dialog
                            }
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }


    private fun showErrorDialog() {
        viewModelScope.launch {
            _state.update {
                it.copy(isErrorDialogShowing = true)
            }
        }
    }

    private fun onClickQuit() {
        viewModelScope.launch {
            _state.update {
                it.copy(isErrorDialogShowing = false)
            }

            _effect.emit(StudioDetailContract.Effect.Quit)
        }
    }

    private fun onClickInfoTab() {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.ScrollToInfoTab)
        }
    }

    private fun onClickReviewTab() {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.ScrollToReviewTab)
        }
    }

    private fun onScrolledAtInfoArea() {
        viewModelScope.launch {
            _state.update {
                it.copy(focusedTab = StudioDetailTabType.Info)
            }
        }
    }

    private fun onScrolledAtReviewArea() {
        viewModelScope.launch {
            _state.update {
                it.copy(focusedTab = StudioDetailTabType.Review)
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

    private fun onClickEditInfoSuggestion() {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.GoToRegisterStudio)
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
                val filteredList = state.value.originalReviewList.filter { review -> review.filesPath?.isNotEmpty() == true }
                it.copy(
                    isReviewListShowingImageReviewsOnly = true,
                    reviewList = if (state.value.isReviewListSortedByLike) filteredList.sortedByDescending { review -> review.likeCount }
                    else filteredList.sortedByDescending { review -> review.createdAt })
            }
        }
    }

    private fun onClickShowNotOnlyImageReviews() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isReviewListShowingImageReviewsOnly = false,
                    reviewList = if (state.value.isReviewListSortedByLike) state.value.originalReviewList.sortedByDescending { review -> review.likeCount }
                    else state.value.originalReviewList.sortedByDescending { review -> review.createdAt })
            }
        }
    }

    private fun onClickSortByLike() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    reviewList = state.value.reviewList.sortedByDescending { review -> review.likeCount },
                    isReviewListSortedByLike = true
                )
            }
        }
    }

    private fun onClickSortByDate() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    reviewList = state.value.reviewList.sortedByDescending { review -> review.createdAt },
                    isReviewListSortedByLike = false
                )
            }
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

    private fun onClickWriteReview() {
        state.value.studio?.id?.let { studioId ->
            viewModelScope.launch {
                _effect.emit(StudioDetailContract.Effect.GoToWriteReview(studioId))
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

    private fun onClickReviewLikeButton(reviewId: Int) {
        state.value.reviewList.find { review -> review.id == reviewId }?.let { selectedReview ->
            viewModelScope.launch(ioDispatcher + exceptionHandler) {
                when (selectedReview.liked) {
                    true -> {
                        state.value.studio?.id?.let { studioId ->
                            dislikeReviewUseCase(studioId, reviewId).onEach { result ->
                                withContext(mainImmediateDispatcher) {
                                    when (result) {
                                        is ResponseState.Success -> {
                                            _state.update {
                                                it.copy(reviewList = sortRefreshedReviewList(state.value.reviewList.toMutableList().apply {
                                                    val targetIndex = indexOf(find { review -> review.id == reviewId })
                                                    this[targetIndex] = this[targetIndex].copy(liked = false, likeCount = this[targetIndex].likeCount?.minus(1))
                                                }))
                                            }
                                        }

                                        is ResponseState.Error -> {
                                            _effect.emit(StudioDetailContract.Effect.ShowToast("리뷰 추천을 취소할 수 없습니다."))
                                        }
                                    }
                                }
                            }.launchIn(viewModelScope)
                        }
                    }

                    false -> {
                        state.value.studio?.id?.let { studioId ->
                            likeReviewUseCase(studioId, reviewId).onEach { result ->
                                withContext(mainImmediateDispatcher) {
                                    when (result) {
                                        is ResponseState.Success -> {
                                            _state.update {
                                                it.copy(reviewList = sortRefreshedReviewList(state.value.reviewList.toMutableList().apply {
                                                    val targetIndex = indexOf(find { review -> review.id == reviewId })
                                                    this[targetIndex] = this[targetIndex].copy(liked = true, likeCount = this[targetIndex].likeCount?.plus(1))
                                                }))
                                            }
                                        }

                                        is ResponseState.Error -> {
                                            _effect.emit(StudioDetailContract.Effect.ShowToast("리뷰를 추천할 수 없습니다."))
                                        }
                                    }
                                }
                            }.launchIn(viewModelScope)
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

    private fun onClickScrap() {
        state.value.studio?.id?.let { studioId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher + exceptionHandler) {
                    when (state.value.studio?.scrapped) {
                        true -> {
                            scrapCancelUseCase(studioId).onEach { result ->
                                withContext(mainImmediateDispatcher) {
                                    when (result) {
                                        is ResponseState.Success -> {
                                            _state.update {
                                                it.copy(
                                                    isLoading = false,
                                                    studio = it.studio?.copy(
                                                        scrapCount = it.studio.scrapCount?.minus(1),
                                                        scrapped = false
                                                    )
                                                )
                                            }
                                        }

                                        is ResponseState.Error -> {
                                            _state.update {
                                                it.copy(isLoading = false)
                                            }

                                            _effect.emit(StudioDetailContract.Effect.ShowToast("스크랩을 취소할 수 없습니다."))
                                        }
                                    }
                                }
                            }.launchIn(viewModelScope)
                        }
                        false -> {
                            scrapUseCase(studioId).onEach { result ->
                                withContext(mainImmediateDispatcher) {
                                    when (result) {
                                        is ResponseState.Success -> {
                                            _state.update {
                                                it.copy(
                                                    isLoading = false,
                                                    studio = it.studio?.copy(
                                                        scrapCount = it.studio.scrapCount?.plus(1),
                                                        scrapped = true
                                                    )
                                                )
                                            }
                                        }

                                        is ResponseState.Error -> {
                                            _state.update {
                                                it.copy(isLoading = false)
                                            }

                                            _effect.emit(StudioDetailContract.Effect.ShowToast("스크랩 할 수 없습니다."))
                                        }
                                    }
                                }
                            }.launchIn(viewModelScope)
                        }
                        null -> {
                            _state.update {
                                it.copy(isLoading = false)
                            }

                            _effect.emit(StudioDetailContract.Effect.ShowToast("스크랩 기능을 사용할 수 없습니다."))
                        }
                    }
                }
            }
        }
    }
}