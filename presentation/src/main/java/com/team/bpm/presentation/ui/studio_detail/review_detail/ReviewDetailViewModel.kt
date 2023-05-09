package com.team.bpm.presentation.ui.studio_detail.review_detail

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.usecase.review.GetReviewDetailUseCase
import com.team.bpm.domain.usecase.review.like.DislikeReviewUseCase
import com.team.bpm.domain.usecase.review.like.LikeReviewUseCase
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
class ReviewDetailViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getReviewDetailUseCase: GetReviewDetailUseCase,
    private val likeReviewUseCase: LikeReviewUseCase,
    private val dislikeReviewUseCase: DislikeReviewUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), ReviewDetailContract {

    private val _state = MutableStateFlow(ReviewDetailContract.State())
    override val state: StateFlow<ReviewDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ReviewDetailContract.Effect>()
    override val effect: SharedFlow<ReviewDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: ReviewDetailContract.Event) = when (event) {
        is ReviewDetailContract.Event.GetReviewDetail -> {
            getReviewDetail()
        }
        is ReviewDetailContract.Event.OnClickLike -> {
            onClickLike()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getBundle(): Bundle? {
        return savedStateHandle.get<Bundle>(ReviewDetailActivity.KEY_BUNDLE)
    }

    private val reviewInfo: Pair<Int, Int> by lazy {
        Pair(
            getBundle()?.getInt(ReviewDetailActivity.KEY_STUDIO_ID) ?: 0,
            getBundle()?.getInt(ReviewDetailActivity.KEY_REVIEW_ID) ?: 0
        )
    }

    private fun getReviewDetail() {
        _state.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            getReviewDetailUseCase(studioId = reviewInfo.first, reviewId = reviewInfo.second).onEach { result ->
                when (result) {
                    is ResponseState.Success -> {
                        _state.update {
                            it.copy(isLoading = false, review = result.data, liked = result.data.liked, likeCount = result.data.likeCount)
                        }
                    }
                    is ResponseState.Error -> {
                        // TODO : Show Error Dialog
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun onClickLike() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            state.value.liked?.let {
                when (it) {
                    true -> {
                        dislikeReviewUseCase(reviewInfo.first, reviewInfo.second).onEach { result ->
                            withContext(mainImmediateDispatcher) {
                                var toastText = ""
                                when (result) {
                                    is ResponseState.Success -> {
                                        toastText = "리뷰 추천을 취소하였습니다."
                                        _state.update { state -> state.copy(isLoading = false, liked = false, likeCount = state.likeCount?.minus(1)) }
                                    }
                                    is ResponseState.Error -> {
                                        toastText = "리뷰 추천을 취소할 수 없습니다."
                                        _state.update { state -> state.copy(isLoading = false) }
                                    }
                                }

                                _effect.emit(ReviewDetailContract.Effect.ShowToast(toastText))
                            }
                        }.launchIn(viewModelScope)
                    }
                    false -> {
                        likeReviewUseCase(reviewInfo.first, reviewInfo.second).onEach { result ->
                            withContext(mainImmediateDispatcher) {
                                var toastText = ""
                                when (result) {
                                    is ResponseState.Success -> {
                                        toastText = "리뷰를 추천하였습니다."
                                        _state.update { state -> state.copy(isLoading = false, liked = true, likeCount = state.likeCount?.plus(1)) }
                                    }
                                    is ResponseState.Error -> {
                                        toastText = "리뷰를 추천할 수 없습니다."
                                        _state.update { state -> state.copy(isLoading = false) }
                                    }
                                }

                                _effect.emit(ReviewDetailContract.Effect.ShowToast(toastText))
                            }
                        }.launchIn(viewModelScope)
                    }
                }
            }
        }
    }
}