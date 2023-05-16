package com.team.bpm.presentation.ui.studio_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.usecase.review.GetReviewListUseCase
import com.team.bpm.domain.usecase.studio_detail.StudioDetailUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import com.team.bpm.presentation.model.StudioDetailTabType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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
                                _state.update {
                                    it.copy(isReviewLoading = false, originalReviewList = result.data, reviewList = sortRefreshedReviewList(result.data))
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

    private fun onClickExpandTagList() {
        viewModelScope.launch {
            _state.update {
                it.copy(isTagListExpanded = true)
            }
        }
    }

    private fun onClickCollapseTagList() {
        _state.update {
            it.copy(isTagListExpanded = false)
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
}