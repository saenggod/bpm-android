package com.team.bpm.presentation.ui.studio_detail.review_detail

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.review.*
import com.team.bpm.domain.usecase.splash.GetKakaoIdUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.ui.studio_detail.StudioDetailContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ReviewDetailViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getKakaoIdUseCase: GetKakaoIdUseCase,
    private val getReviewDetailUseCase: GetReviewDetailUseCase,
    private val likeReviewUseCase: LikeReviewUseCase,
    private val dislikeReviewUseCase: DislikeReviewUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val reportReviewUseCase: ReportReviewUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), ReviewDetailContract {

    private val _state = MutableStateFlow(ReviewDetailContract.State())
    override val state: StateFlow<ReviewDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ReviewDetailContract.Effect>()
    override val effect: SharedFlow<ReviewDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: ReviewDetailContract.Event) = when (event) {
        is ReviewDetailContract.Event.GetUserId -> {
            getUserId()
        }

        is ReviewDetailContract.Event.GetReviewDetail -> {
            getReviewDetail()
        }

        is ReviewDetailContract.Event.OnClickLike -> {
            onClickLike()
        }

        is ReviewDetailContract.Event.OnClickReviewActionButton -> {
            onClickReviewActionButton()
        }

        is ReviewDetailContract.Event.OnClickDeleteReview -> {
            onClickDeleteReview()
        }

        is ReviewDetailContract.Event.OnClickReportReview -> {
            onClickReportReview()
        }

        is ReviewDetailContract.Event.OnClickSendReviewReport -> {
            onClickSendReviewReport(event.reason)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getBundle(): Bundle? {
        return savedStateHandle.get<Bundle>(ReviewDetailActivity.KEY_BUNDLE)
    }

    private val reviewInfo: Pair<Int?, Int?> by lazy {
        Pair(
            getBundle()?.getInt(ReviewDetailActivity.KEY_STUDIO_ID) ?: 1,
            getBundle()?.getInt(ReviewDetailActivity.KEY_REVIEW_ID) ?: 66
        )
    }

    private fun getUserId() {
        viewModelScope.launch(ioDispatcher) {
            getKakaoIdUseCase().onEach { result ->
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

    private fun getReviewDetail() {
        reviewInfo.first?.let { studioId ->
            reviewInfo.second?.let { reviewId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }

                    withContext(ioDispatcher) {
                        getReviewDetailUseCase(studioId, reviewId).onEach { result ->
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        review = result,
                                        liked = result.liked,
                                        likeCount = result.likeCount
                                    )
                                }
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }
                }
            }
        }
    }

    private fun onClickLike() {
        reviewInfo.first?.let { studioId ->
            reviewInfo.second?.let { reviewId ->
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    withContext(ioDispatcher) {
                        state.value.liked?.let {
                            when (it) {
                                true -> {
                                    dislikeReviewUseCase(studioId, reviewId).onEach { result ->
                                        withContext(mainImmediateDispatcher) {
                                            _state.update { state ->
                                                state.copy(
                                                    isLoading = false,
                                                    liked = false,
                                                    likeCount = state.likeCount?.minus(1)
                                                )
                                            }

                                            _effect.emit(ReviewDetailContract.Effect.ShowToast("리뷰 추천을 취소하였습니다."))
                                        }
                                    }.launchIn(viewModelScope + exceptionHandler)
                                }

                                false -> {
                                    likeReviewUseCase(studioId, reviewId).onEach { result ->
                                        withContext(mainImmediateDispatcher) {
                                            _state.update { state ->
                                                state.copy(
                                                    isLoading = false,
                                                    liked = true,
                                                    likeCount = state.likeCount?.plus(1)
                                                )
                                            }

                                            _effect.emit(ReviewDetailContract.Effect.ShowToast("리뷰를 추천하였습니다."))
                                        }
                                    }.launchIn(viewModelScope + exceptionHandler)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onClickReviewActionButton() {
        state.value.review?.author?.id?.let { reviewAuthorId ->
            viewModelScope.launch {
                _state.update {
                    val bottomSheetButtonList = mutableListOf<BottomSheetButton>().apply {
                        if (reviewAuthorId == state.value.userId) {
                            add(BottomSheetButton.DELETE_POST)
                        } else {
                            add(BottomSheetButton.REPORT_POST)
                        }
                    }

                    it.copy(
                        bottomSheetButtonList = bottomSheetButtonList,
                        isBottomSheetShowing = true
                    )
                }
            }
        }
    }

    private fun onClickDeleteReview() {
        reviewInfo.first?.let { studioId ->
            reviewInfo.second?.let { reviewId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }

                    withContext(ioDispatcher) {
                        deleteReviewUseCase(studioId, reviewId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _effect.emit(ReviewDetailContract.Effect.GoBack)
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }
                }
            }
        }
    }

    private fun onClickReportReview() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isBottomSheetShowing = false,
                    isReportDialogShowing = true
                )
            }
        }
    }

    private fun onClickSendReviewReport(reason: String) {

    }
}