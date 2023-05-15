package com.team.bpm.presentation.ui.studio_detail.review_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.usecase.review.GetReviewListUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReviewListViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val reviewListUseCase: GetReviewListUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), ReviewListContract {

    private val _state = MutableStateFlow(ReviewListContract.State())
    override val state: StateFlow<ReviewListContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ReviewListContract.Effect>()
    override val effect: SharedFlow<ReviewListContract.Effect> = _effect.asSharedFlow()

    override fun event(event: ReviewListContract.Event) = when (event) {
        ReviewListContract.Event.GetReviewList -> {
            getReviewList()
        }

        ReviewListContract.Event.OnClickShowImageReviewsOnly -> {
            onClickShowImageReviewsOnly()
        }

        ReviewListContract.Event.OnClickShowNotOnlyImageReviews -> {
            onClickShowNotOnlyImageReviews()
        }

        ReviewListContract.Event.OnClickSortByLike -> {
            onClickSortByLike()
        }

        ReviewListContract.Event.OnClickSortByDate -> {
            onClickSortByDate()
        }

        ReviewListContract.Event.OnClickWriteReview -> {
            onClickWriteReview()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getStudioId(): Int? {
        return savedStateHandle.get<Int>(ReviewListActivity.KEY_STUDIO_ID)
    }

    private fun getReviewList() {
        getStudioId()?.let { studioId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher + exceptionHandler) {
                    reviewListUseCase(studioId).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            when (result) {
                                is ResponseState.Success -> {
                                    _state.update {
                                        it.copy(isLoading = false, originalReviewList = result.data, reviewList = result.data.sortedByDescending { review -> review.likeCount })
                                    }
                                }

                                is ResponseState.Error -> {
                                    // TODO : Show error dialog
                                }
                            }
                        }
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    private fun onClickShowImageReviewsOnly() {
        _state.update {
            val filteredList = state.value.originalReviewList.filter { review -> review.filesPath?.isNotEmpty() == true }
            it.copy(
                isReviewListShowingImageReviewsOnly = true,
                reviewList = if (state.value.isReviewListSortedByLike) filteredList.sortedByDescending { review -> review.likeCount }
                else filteredList.sortedByDescending { review -> review.createdAt })
        }
    }

    private fun onClickShowNotOnlyImageReviews() {
        _state.update {
            it.copy(
                isReviewListShowingImageReviewsOnly = false,
                reviewList = if (state.value.isReviewListSortedByLike) state.value.originalReviewList.sortedByDescending { review -> review.likeCount }
                else state.value.originalReviewList.sortedByDescending { review -> review.createdAt })
        }
    }

    private fun onClickSortByLike() {
        _state.update {
            it.copy(
                reviewList = state.value.reviewList.sortedByDescending { review -> review.likeCount },
                isReviewListSortedByLike = true
            )
        }
    }

    private fun onClickSortByDate() {
        _state.update {
            it.copy(
                reviewList = state.value.reviewList.sortedByDescending { review -> review.createdAt },
                isReviewListSortedByLike = false
            )
        }
    }

    private fun onClickWriteReview() {
        getStudioId()?.let { studioId ->
            viewModelScope.launch {
                _effect.emit(ReviewListContract.Effect.GoToWriteReview(studioId))
            }
        }
    }
}