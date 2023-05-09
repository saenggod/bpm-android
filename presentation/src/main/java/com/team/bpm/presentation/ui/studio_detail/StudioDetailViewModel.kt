package com.team.bpm.presentation.ui.studio_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.Studio
import com.team.bpm.domain.usecase.review.GetReviewListUseCase
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
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), StudioDetailContract {

    private val _state = MutableStateFlow(StudioDetailContract.State())
    override val state: StateFlow<StudioDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<StudioDetailContract.Effect>()
    override val effect: SharedFlow<StudioDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: StudioDetailContract.Event) = when (event) {
        is StudioDetailContract.Event.GetStudioDetailData -> {
            getStudioId()?.let { getStudioDetailData(it) } ?: run {
                // TODO : Error Handling
            }
        }
        is StudioDetailContract.Event.ShowErrorDialog -> {
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
        is StudioDetailContract.Event.OnClickCopyAddress -> {
            onClickCopyAddress(event.address)
        }
        is StudioDetailContract.Event.OnClickCall -> {
            onClickCall(event.number)
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
        is StudioDetailContract.Event.OnClickExpandTagList -> {
            onClickExpandTagList()
        }
        is StudioDetailContract.Event.OnClickCollapseTagList -> {
            onClickCollapseTagList()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getStudioId(): Int? {
        return savedStateHandle.get<Int>(StudioDetailActivity.KEY_STUDIO_ID)
    }

    private fun getStudioDetailData(studioId: Int) {
        _state.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            studioDetailUseCase(studioId).zip(reviewListUseCase(studioId)) { studioResult, reviewListResult ->
                Pair(studioResult, reviewListResult)
            }.onEach { pair ->
                withContext(mainImmediateDispatcher) {
                    if (pair.first is ResponseState.Error) {
                        _effect.emit(StudioDetailContract.Effect.LoadFailed)
                    } else {
                        _state.update {
                            it.copy(isLoading = false, studio = (pair.first as ResponseState.Success<Studio>).data)
                        }

                        if (pair.second is ResponseState.Success) {
                            _state.update {
                                val reviewList = (pair.second as ResponseState.Success<List<Review>>).data
                                it.copy(originalReviewList = reviewList, reviewList = reviewList)
                            }
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun showErrorDialog() {
        _state.update {
            it.copy(isErrorDialogShowing = true)
        }
    }

    private fun onClickQuit() {
        _state.update {
            it.copy(isErrorDialogShowing = false)
        }

        viewModelScope.launch {
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
        _state.update {
            it.copy(focusedTab = StudioDetailTabType.Info)
        }
    }

    private fun onScrolledAtReviewArea() {
        _state.update {
            it.copy(focusedTab = StudioDetailTabType.Review)
        }
    }

    private fun onClickCopyAddress(address: String) {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.CopyAddressToClipboard(address))
        }
    }

    private fun onClickCall(number: String) {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.Call(number))
        }
    }

    private fun onClickEditInfoSuggestion() {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.GoToRegisterStudio)
        }
    }

    private fun onClickWriteReview() {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.GoToWriteReview)
        }
    }

    private fun onClickMoreReviews() {
        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.GoToReviewList)
        }
    }

    private fun onClickShowImageReviewsOnly() {
        state.value.originalReviewList?.let { reviewList ->
            _state.update {
                val filteredList = reviewList.filter { review -> review.filesPath?.isNotEmpty() == true }
                it.copy(reviewList = if (state.value.isReviewListSortedByLike) filteredList.sortedByDescending { review -> review.likeCount }
                else filteredList.sortedByDescending { review -> review.createdAt })
            }
        }
    }

    private fun onClickShowNotOnlyImageReviews() {
        state.value.originalReviewList?.let { reviewList ->
            _state.update {
                it.copy(reviewList = if (state.value.isReviewListSortedByLike) reviewList.sortedByDescending { review -> review.likeCount }
                else reviewList.sortedByDescending { review -> review.createdAt })
            }
        }
    }

    private fun onClickSortByLike() {
        state.value.originalReviewList?.let { _ ->
            _state.update {
                it.copy(
                    reviewList = state.value.reviewList?.sortedByDescending { review -> review.likeCount },
                    isReviewListSortedByLike = true
                )
            }
        }
    }

    private fun onClickSortByDate() {
        state.value.originalReviewList?.let { _ ->
            _state.update {
                it.copy(
                    reviewList = state.value.reviewList?.sortedByDescending { review -> review.createdAt },
                    isReviewListSortedByLike = false
                )
            }
        }
    }

    private fun onClickExpandTagList() {
        _state.update {
            it.copy(isTagListExpanded = true)
        }
    }

    private fun onClickCollapseTagList() {
        _state.update {
            it.copy(isTagListExpanded = false)
        }
    }
}