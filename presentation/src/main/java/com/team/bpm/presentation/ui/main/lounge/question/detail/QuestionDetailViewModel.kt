package com.team.bpm.presentation.ui.main.lounge.question.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.usecase.question.*
import com.team.bpm.domain.usecase.user.GetUserIdUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.model.ReportType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuestionDetailViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getQuestionDetailUseCase: GetQuestionDetailUseCase,
    private val deleteQuestionUseCase: DeleteQuestionUseCase,
    private val reportQuestionUseCase: ReportQuestionUseCase,
    private val likeQuestionUseCase: LikeQuestionUseCase,
    private val dislikeQuestionUseCase: DislikeQuestionUseCase,
    private val writeQuestionCommentUseCase: WriteQuestionCommentUseCase,
    private val getQuestionCommentListUseCase: GetQuestionCommentListUseCase,
    private val deleteQuestionCommentUseCase: DeleteQuestionCommentUseCase,
    private val reportQuestionCommentUseCase: ReportQuestionCommentUseCase,
    private val likeQuestionCommentUseCase: LikeQuestionCommentUseCase,
    private val dislikeQuestionCommentUseCase: DislikeQuestionCommentUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModelV2(), QuestionDetailContract {

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

        is QuestionDetailContract.Event.OnClickQuestionActionButton -> {
            onClickQuestionActionButton()
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

        is QuestionDetailContract.Event.OnClickLike -> {
            onClickLike()
        }

        is QuestionDetailContract.Event.OnClickSendComment -> {
            onClickSendComment(
                parentId = event.parentId,
                comment = event.comment
            )
        }

        is QuestionDetailContract.Event.GetCommentList -> {
            getCommentList()
        }

        is QuestionDetailContract.Event.OnClickCommentActionButton -> {
            onClickCommentActionButton(
                selectedComment = event.comment,
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

        is QuestionDetailContract.Event.OnClickCommentLike -> {
            onClickCommentLike(event.commentId)
        }

        is QuestionDetailContract.Event.OnClickDismissNoticeToQuitDialog -> {
            onClickDismissNoticeToQuitDialog()
        }

        is QuestionDetailContract.Event.OnBottomSheetHide -> {
            onBottomSheetHide()
        }

        is QuestionDetailContract.Event.OnClickCancelReplying -> {
            onClickCancelReplying()
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

    private fun getQuestionId(): Int? {
        return savedStateHandle.get<Int>(QuestionDetailActivity.KEY_QUESTION_ID)
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

    private fun onClickQuestionActionButton() {
        state.value.question?.author?.id?.let { questionAuthorId ->
            viewModelScope.launch {
                _state.update {
                    val bottomSheetButtonList = mutableListOf<BottomSheetButton>().apply {
                        if (questionAuthorId == it.userId) {
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

    private fun onClickDeleteQuestion() {
        getQuestionId()?.let { questionId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        isLoading = true,
                        isBottomSheetShowing = false
                    )
                }

                withContext(ioDispatcher) {
                    deleteQuestionUseCase(questionId).onEach {
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

    private fun onClickReportQuestion() {
        viewModelScope.launch {
            if (state.value.isReplying) {
                _effect.emit(QuestionDetailContract.Effect.StopReplying)
            }

            _state.update {
                it.copy(
                    reportType = ReportType.POST,
                    isReportDialogShowing = true,
                    isBottomSheetShowing = false,
                    isReplying = false,
                    isReporting = true
                )
            }
        }
    }

    private fun onClickSendQuestionReport(reason: String) {
        getQuestionId()?.let { questionId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        isLoading = true,
                        isReportDialogShowing = false
                    )
                }

                withContext(ioDispatcher) {
                    reportQuestionUseCase(questionId, reason).onEach {
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isNoticeToQuitDialogShowing = true,
                                    noticeToQuitDialogContent = "신고가 완료되었습니다.",
                                    isReporting = false
                                )
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }

    private fun onClickLike() {
        getQuestionId()?.let { questionId ->
            viewModelScope.launch(ioDispatcher) {
                state.value.liked?.let {
                    when (it) {
                        true -> {
                            dislikeQuestionUseCase(questionId).onEach {
                                withContext(mainImmediateDispatcher) {
                                    _state.update {
                                        it.copy(
                                            liked = false,
                                            likeCount = it.likeCount?.minus(1)
                                        )
                                    }
                                }
                            }.launchIn(viewModelScope + exceptionHandler)
                        }

                        false -> {
                            likeQuestionUseCase(questionId).onEach {
                                withContext(mainImmediateDispatcher) {
                                    _state.update {
                                        it.copy(
                                            liked = true,
                                            likeCount = it.likeCount?.plus(1)
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

    private fun onClickSendComment(parentId: Int?, comment: String) {
        getQuestionId()?.let { questionId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    writeQuestionCommentUseCase(questionId, parentId, comment).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    commentIdToScroll = result.id,
                                    selectedComment = null,
                                    parentCommentId = null,
                                    isReplying = false
                                )
                            }

                            _effect.emit(QuestionDetailContract.Effect.RefreshCommentList)
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
                                result.comments?.filter { it.reported == false }?.forEach { comment ->
                                    add(comment)

                                    comment.children?.let { childrenCommentList ->
                                        childrenCommentList.filter { it.reported == false }.forEach { childComment ->
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

    private fun onClickCommentActionButton(
        selectedComment: Comment,
        parentCommentId: Int?
    ) {
        selectedComment.id?.let { commentId ->
            selectedComment.author?.id?.let { authorId ->
                viewModelScope.launch {
                    _state.update {
                        val bottomSheetButtonList = mutableListOf<BottomSheetButton>().apply {
                            add(BottomSheetButton.REPLY_COMMENT)
                            if (authorId == it.userId) {
                                add(BottomSheetButton.DELETE_COMMENT)
                            } else {
                                add(BottomSheetButton.REPORT_COMMENT)
                            }
                        }

                        it.copy(
                            selectedComment = selectedComment,
                            commentIdToScroll = commentId,
                            parentCommentId = parentCommentId,
                            bottomSheetButtonList = bottomSheetButtonList,
                            isBottomSheetShowing = true
                        )
                    }
                }
            }
        }
    }

    private fun onClickReplyComment() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isBottomSheetShowing = false,
                    isReplying = true
                )
            }

            _effect.emit(QuestionDetailContract.Effect.SetUpToReply)
        }
    }

    private fun onClickDeleteComment() {
        getQuestionId()?.let { questionId ->
            state.value.selectedComment?.id?.let { selectedCommentId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isCommentListLoading = true,
                            isBottomSheetShowing = false
                        )
                    }

                    withContext(ioDispatcher) {
                        deleteQuestionCommentUseCase(questionId, selectedCommentId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        isCommentListLoading = false,
                                        isNoticeDialogShowing = true,
                                        noticeDialogContent = "삭제가 완료되었습니다."
                                    )
                                }

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
            if (state.value.isReplying) {
                _effect.emit(QuestionDetailContract.Effect.StopReplying)
            }

            _state.update {
                it.copy(
                    reportType = ReportType.COMMENT,
                    isReportDialogShowing = true,
                    isBottomSheetShowing = false,
                    isReplying = false,
                    isReporting = true
                )
            }
        }
    }

    private fun onClickSendCommentReport(reason: String) {
        getQuestionId()?.let { questionId ->
            state.value.selectedComment?.id?.let { selectedCommentId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isCommentListLoading = true,
                            isReportDialogShowing = false,
                            isReporting = false
                        )
                    }

                    withContext(ioDispatcher) {
                        reportQuestionCommentUseCase(questionId, selectedCommentId, reason).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        isCommentListLoading = false,
                                        isNoticeDialogShowing = true,
                                        noticeDialogContent = "신고가 완료되었습니다.",
                                    )
                                }

                                _effect.emit(QuestionDetailContract.Effect.RefreshCommentList)
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
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
                                    it.copy(commentList = it.commentList.toMutableList().apply {
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
                                    it.copy(commentList = it.commentList.toMutableList().apply {
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

    private fun onClickDismissReportDialog() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isReportDialogShowing = false,
                    isReporting = false
                )
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

    private fun onClickDismissNoticeToQuitDialog() {
        viewModelScope.launch {
            _effect.emit(QuestionDetailContract.Effect.GoToQuestionList)
        }
    }

    private fun onBottomSheetHide() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isBottomSheetShowing = false,
                    selectedComment = if (it.isReplying || it.isReporting) it.selectedComment else null,
                    parentCommentId = if (it.isReplying) it.parentCommentId else null
                )
            }
        }
    }

    private fun onClickCancelReplying() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isReplying = false,
                    selectedComment = null,
                    parentCommentId = null
                )
            }

            _effect.emit(QuestionDetailContract.Effect.StopReplying)
        }
    }
}