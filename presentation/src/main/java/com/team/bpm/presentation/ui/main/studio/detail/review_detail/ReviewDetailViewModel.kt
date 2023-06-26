package com.team.bpm.presentation.ui.main.studio.detail.review_detail

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.review.*
import com.team.bpm.domain.usecase.user.GetUserIdUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import com.team.bpm.presentation.model.BottomSheetButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReviewDetailViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val reportReviewUseCase: ReportReviewUseCase,
    private val likeReviewUseCase: LikeReviewUseCase,
    private val dislikeReviewUseCase: DislikeReviewUseCase,
    private val getReviewDetailUseCase: GetReviewDetailUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModelV2(), ReviewDetailContract {

    private val _state = MutableStateFlow(ReviewDetailContract.State())
    override val state: StateFlow<ReviewDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ReviewDetailContract.Effect>()
    override val effect: SharedFlow<ReviewDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: ReviewDetailContract.Event) = when (event) {
        is ReviewDetailContract.Event.GetUserId -> {
            getUserId()
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

        is ReviewDetailContract.Event.GetReviewDetail -> {
            getReviewDetail()
        }

        is ReviewDetailContract.Event.OnClickDismissReportDialog -> {
            onClickDismissReportDialog()
        }

        is ReviewDetailContract.Event.OnClickDismissNoticeDialog -> {
            onClickDismissNoticeDialog()
        }

        ReviewDetailContract.Event.OnClickDismissNoticeToQuitDialog -> {
            onClickDismissNoticeToQuitDialog()
        }

        is ReviewDetailContract.Event.OnBottomSheetHide -> {
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

    private fun getBundle(): Bundle? {
        return savedStateHandle.get<Bundle>(ReviewDetailActivity.KEY_BUNDLE)
    }

    private val reviewInfo: Pair<Int?, Int?> by lazy {
        Pair(
            getBundle()?.getInt(ReviewDetailActivity.KEY_STUDIO_ID),
            getBundle()?.getInt(ReviewDetailActivity.KEY_REVIEW_ID)
        )
    }

    private fun onClickReviewActionButton() {
        state.value.review?.author?.id?.let { reviewAuthorId ->
            viewModelScope.launch {
                _state.update {
                    val bottomSheetButtonList = mutableListOf<BottomSheetButton>().apply {
                        if (reviewAuthorId == it.userId) {
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
                        it.copy(
                            isLoading = true,
                            isBottomSheetShowing = false
                        )
                    }

                    withContext(ioDispatcher) {
                        deleteReviewUseCase(studioId, reviewId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        isNoticeToQuitDialogShowing = true,
                                        noticeToQuitDialogContent = "삭제가 완료되었습니다."
                                    )
                                }
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
        reviewInfo.first?.let { studioId ->
            reviewInfo.second?.let { reviewId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            isReportDialogShowing = false
                        )
                    }

                    withContext(ioDispatcher) {
                        reportReviewUseCase(studioId, reviewId, reason).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        isNoticeToQuitDialogShowing = true,
                                        noticeToQuitDialogContent = "신고가 완료되었습니다."
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
                viewModelScope.launch(ioDispatcher) {
                    state.value.liked?.let {
                        when (it) {
                            true -> {
                                dislikeReviewUseCase(studioId, reviewId).onEach { result ->
                                    withContext(mainImmediateDispatcher) {
                                        _state.update { state ->
                                            state.copy(
                                                liked = false,
                                                likeCount = state.likeCount?.minus(1)
                                            )
                                        }
                                    }
                                }.launchIn(viewModelScope + exceptionHandler)
                            }

                            false -> {
                                likeReviewUseCase(studioId, reviewId).onEach { result ->
                                    withContext(mainImmediateDispatcher) {
                                        _state.update { state ->
                                            state.copy(
                                                liked = true,
                                                likeCount = state.likeCount?.plus(1)
                                            )
                                        }
                                    }
                                }.launchIn(viewModelScope + exceptionHandler)
                            }
                        }
                    }
                }
            }
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

    private fun onClickDismissReportDialog() {
        viewModelScope.launch {
            _state.update { it.copy(isReportDialogShowing = false) }
        }
    }

    private fun onClickDismissNoticeDialog() {
        viewModelScope.launch {
            _state.update {
                it.copy(isNoticeDialogShowing = false)
            }
        }
    }

    private fun onClickDismissNoticeToQuitDialog() {
        viewModelScope.launch {
            _effect.emit(ReviewDetailContract.Effect.GoBack)
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