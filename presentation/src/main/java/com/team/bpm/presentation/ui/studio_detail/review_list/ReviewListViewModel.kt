package com.team.bpm.presentation.ui.studio_detail.review_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.usecase.review.GetReviewListUseCase
import com.team.bpm.domain.usecase.review.like.DislikeReviewUseCase
import com.team.bpm.domain.usecase.review.like.LikeReviewUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
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
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReviewListViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val reviewListUseCase: GetReviewListUseCase,
    private val likeReviewUseCase: LikeReviewUseCase,
    private val dislikeReviewUseCase: DislikeReviewUseCase,
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

        is ReviewListContract.Event.OnClickReviewLikeButton -> {
            onClickReviewLikeButton(event.reviewId)
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

                withContext(ioDispatcher) {
                    reviewListUseCase(studioId).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(isLoading = false, originalReviewList = result.reviews ?: emptyList(), reviewList = result.reviews?.let { reviews -> sortRefreshedReviewList(reviews) } ?: emptyList()
                                )
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
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

    private fun onClickWriteReview() {
        getStudioId()?.let { studioId ->
            viewModelScope.launch {
                _effect.emit(ReviewListContract.Effect.GoToWriteReview(studioId))
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
            viewModelScope.launch(ioDispatcher) {
                when (selectedReview.liked) {
                    true -> {
                        getStudioId()?.let { studioId ->
                            dislikeReviewUseCase(studioId, reviewId).onEach {
                                withContext(mainImmediateDispatcher) {
                                    _state.update {
                                        it.copy(reviewList = sortRefreshedReviewList(state.value.reviewList.toMutableList().apply {
                                            val targetIndex = indexOf(find { review -> review.id == reviewId })
                                            this[targetIndex] = this[targetIndex].copy(liked = false, likeCount = this[targetIndex].likeCount?.minus(1))
                                        }))
                                    }
                                }
                            }.launchIn(viewModelScope + exceptionHandler)
                        }
                    }

                    false -> {
                        getStudioId()?.let { studioId ->
                            likeReviewUseCase(studioId, reviewId).onEach {
                                withContext(mainImmediateDispatcher) {
                                    _state.update {
                                        it.copy(reviewList = sortRefreshedReviewList(state.value.reviewList.toMutableList().apply {
                                            val targetIndex = indexOf(find { review -> review.id == reviewId })
                                            this[targetIndex] = this[targetIndex].copy(liked = true, likeCount = this[targetIndex].likeCount?.plus(1))
                                        }))
                                    }
                                }
                            }.launchIn(viewModelScope + exceptionHandler)
                        }
                    }

                    null -> {
                        withContext(mainImmediateDispatcher) {
                            _effect.emit(ReviewListContract.Effect.ShowToast("좋아요 기능을 사용할 수 없습니다."))
                        }
                    }
                }
            }
        }
    }
}