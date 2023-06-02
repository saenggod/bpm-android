package com.team.bpm.presentation.ui.main.community.question_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.usecase.question.*
import com.team.bpm.domain.usecase.splash.GetKakaoIdUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import com.team.bpm.presentation.model.BottomSheetButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class QuestionDetailViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getQuestionDetailUseCase: GetQuestionDetailUseCase,
    private val getQuestionCommentListUseCase: GetQuestionCommentListUseCase,
    private val deleteQuestionUseCase: DeleteQuestionUseCase,
    private val reportQuestionUseCase: ReportQuestionUseCase,
    private val writeQuestionCommentUseCase: WriteQuestionCommentUseCase,
    private val likeQuestionUseCase: LikeQuestionUseCase,
    private val dislikeQuestionUseCase: DislikeQuestionUseCase,
    private val deleteQuestionCommentUseCase: DeleteQuestionCommentUseCase,
    private val reportQuestionCommentUseCase: ReportQuestionCommentUseCase,
    private val likeQuestionCommentUseCase: LikeQuestionCommentUseCase,
    private val dislikeQuestionCommentUseCase: DislikeQuestionCommentUseCase,
    private val getKakaoIdUseCase: GetKakaoIdUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), QuestionDetailContract {

    private val _state = MutableStateFlow(QuestionDetailContract.State())
    override val state: StateFlow<QuestionDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<QuestionDetailContract.Effect>()
    override val effect: SharedFlow<QuestionDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: QuestionDetailContract.Event) = when (event) {
        is QuestionDetailContract.Event.GetUserId -> {
            getUserId()
        }

        is QuestionDetailContract.Event.GetQuestionDetail -> {
            getQuestionDetail()
        }

        is QuestionDetailContract.Event.GetCommentList -> {
            getCommentList()
        }

        is QuestionDetailContract.Event.OnClickDeleteQuestion -> {
            onClickDeleteQuestion()
        }

        is QuestionDetailContract.Event.OnClickReportQuestion -> {
            onClickReportQuestion()
        }

        is QuestionDetailContract.Event.OnClickSendQuestionReport -> {
            onClickSendQuestionReport(event.reason)
        }

        is QuestionDetailContract.Event.OnClickSendComment -> {
            onClickSendComment(parentId = event.parentId, comment = event.comment)
        }

        is QuestionDetailContract.Event.OnClickCommentActionButton -> {
            onClickCommentActionButton(
                selectedCommentId = event.commentId,
                selectedCommentAuthorId = event.authorId,
                parentCommentId = event.parentCommentId
            )
        }

        is QuestionDetailContract.Event.OnClickReplyComment -> {
            onClickReplyComment()
        }

        is QuestionDetailContract.Event.OnClickDeleteComment -> {
            onClickDeleteComment()
        }

        is QuestionDetailContract.Event.OnClickReportComment -> {
            onClickReportComment()
        }

        is QuestionDetailContract.Event.OnClickDismissReportDialog -> {
            onClickDismissReportDialog()
        }

        is QuestionDetailContract.Event.OnClickDismissNoticeDialog -> {
            onClickDismissNoticeDialog()
        }

        is QuestionDetailContract.Event.OnClickSendCommentReport -> {
            onClickSendCommentReport(event.reason)
        }

        is QuestionDetailContract.Event.OnClickLike -> {
            onClickLike()
        }

        is QuestionDetailContract.Event.OnClickCommentLike -> {
            onClickCommentLike(event.commentId)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getQuestionId(): Int? {
//        return savedStateHandle.get<Int>(QuestionDetailActivity.KEY_QUESTION_ID)
        return 1
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

    private fun getQuestionDetail() {
        getQuestionId()?.let { questionId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    getQuestionDetailUseCase(questionId).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    question = result,
                                    liked = result.favorited,
                                    likeCount = result.favoritesCount
                                )
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }

    private fun getCommentList() {
        getQuestionId()?.let { questionId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isCommentListLoading = true)
                }

                withContext(ioDispatcher) {
                    getQuestionCommentListUseCase(questionId).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            val commentList = mutableListOf<Comment>().apply {
                                result.comments?.forEach { comment ->
                                    add(comment)

                                    comment.children?.let { childrenCommentList ->
                                        childrenCommentList.forEach { childComment ->
                                            add(childComment)
                                        }
                                    }
                                }
                            }

                            _state.update {
                                it.copy(
                                    isCommentListLoading = false,
                                    commentList = commentList,
                                    commentsCount = result.commentsCount ?: result.comments?.size
                                )
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }

    private fun onClickDeleteQuestion() {
        getQuestionId()?.let { questionId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    deleteQuestionUseCase(questionId).onEach {
                        withContext(mainImmediateDispatcher) {
                            _effect.emit(QuestionDetailContract.Effect.GoToQuestionList)
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }

    private fun onClickReportQuestion() {
        // TODO Show Dialog
    }

    private fun onClickSendQuestionReport(reason: String) {
        getQuestionId()?.let { questionId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    reportQuestionUseCase(questionId, reason).onEach {
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isNoticeDialogShowing = true,
                                    noticeDialogContent = "신고가 완료되었습니다"
                                )
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }

    private fun onClickSendComment(parentId: Int?, comment: String) {
        getQuestionId()?.let { questionId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    writeQuestionCommentUseCase(questionId = questionId, parentId = parentId, comment = comment).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    redirectCommentId = result.id,
                                    selectedCommentId = null,
                                    selectedCommentAuthorId = null,
                                    parentCommentId = null
                                )
                            }

                            _effect.emit(QuestionDetailContract.Effect.RefreshCommentList)
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }

    private fun onClickCommentActionButton(
        selectedCommentId: Int,
        selectedCommentAuthorId: Int,
        parentCommentId: Int?
    ) {
        viewModelScope.launch {
            _state.update {
                val bottomSheetButtonList = mutableListOf<BottomSheetButton>().apply {
                    add(BottomSheetButton.REPLY_COMMENT)
                    if (selectedCommentAuthorId.toLong() == state.value.userId) {
                        add(BottomSheetButton.DELETE_COMMENT)
                    } else {
                        add(BottomSheetButton.REPORT_COMMENT)
                    }
                }

                it.copy(
                    selectedCommentId = selectedCommentId,
                    selectedCommentAuthorId = selectedCommentAuthorId,
                    parentCommentId = parentCommentId,
                    bottomSheetButtonList = bottomSheetButtonList
                )
            }

            _effect.emit(QuestionDetailContract.Effect.ExpandBottomSheet)
        }
    }

    private fun onClickReplyComment() {
        viewModelScope.launch {
            _effect.emit(QuestionDetailContract.Effect.ShowKeyboard)
        }
    }

    private fun onClickDeleteComment() {
        getQuestionId()?.let { questionId ->
            state.value.selectedCommentId?.let { selectedCommentId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(isCommentListLoading = true)
                    }

                    withContext(ioDispatcher) {
                        deleteQuestionCommentUseCase(questionId, selectedCommentId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _effect.emit(QuestionDetailContract.Effect.RefreshCommentList)
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }
                }
            }
        }
    }

    private fun onClickReportComment() {
        viewModelScope.launch {
            _state.update {
                it.copy(isReportDialogShowing = true)
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

    private fun onClickDismissNoticeDialog() {
        viewModelScope.launch {
            _state.update {
                it.copy(isNoticeDialogShowing = false)
            }
        }
    }

    private fun onClickSendCommentReport(reason: String) {
        getQuestionId()?.let { questionId ->
            state.value.selectedCommentId?.let { selectedCommentId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(isCommentListLoading = true)
                    }

                    withContext(ioDispatcher) {
                        reportQuestionCommentUseCase(questionId, selectedCommentId, reason).onEach {
                            withContext(mainImmediateDispatcher) {
                                _effect.emit(QuestionDetailContract.Effect.RefreshCommentList)
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }
                }
            }
        }
    }

    private fun onClickLike() {
        getQuestionId()?.let { questionId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    state.value.liked?.let {
                        when (it) {
                            true -> {
                                dislikeQuestionUseCase(questionId).onEach {
                                    withContext(mainImmediateDispatcher) {
                                        _state.update {
                                            it.copy(
                                                isLoading = false,
                                                liked = false,
                                                likeCount = state.value.likeCount?.minus(1)
                                            )
                                        }

                                        _effect.emit(QuestionDetailContract.Effect.ShowToast("질문 추천을 취소하였습니다."))
                                    }
                                }.launchIn(viewModelScope + exceptionHandler)
                            }

                            false -> {
                                likeQuestionUseCase(questionId).onEach {
                                    withContext(mainImmediateDispatcher) {
                                        _state.update {
                                            it.copy(
                                                isLoading = false,
                                                liked = true,
                                                likeCount = state.value.likeCount?.plus(1)
                                            )
                                        }

                                        _effect.emit(QuestionDetailContract.Effect.ShowToast("질문을 추천하였습니다."))
                                    }
                                }.launchIn(viewModelScope + exceptionHandler)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onClickCommentLike(commentId: Int) {
        getQuestionId()?.let { questionId ->
            val comment = state.value.commentList.find { comment -> comment.id == commentId }

            viewModelScope.launch(ioDispatcher) {
                when (comment?.liked) {
                    true -> {
                        dislikeQuestionCommentUseCase(questionId, commentId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(commentList = state.value.commentList.toMutableList().apply {
                                        val targetIndex = indexOf(comment)
                                        this[targetIndex] = this[targetIndex].copy(
                                            liked = false,
                                            likeCount = this[targetIndex].likeCount?.minus(1)
                                        )
                                    })
                                }
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }

                    false -> {
                        likeQuestionCommentUseCase(questionId, commentId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(commentList = state.value.commentList.toMutableList().apply {
                                        val targetIndex = indexOf(comment)
                                        this[targetIndex] = this[targetIndex].copy(
                                            liked = true,
                                            likeCount = this[targetIndex].likeCount?.plus(1)
                                        )
                                    })
                                }
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }

                    null -> {
                        withContext(mainImmediateDispatcher) {
                            _effect.emit(QuestionDetailContract.Effect.ShowToast("좋아요 기능을 사용할 수 없습니다."))
                        }
                    }
                }
            }
        }
    }
}