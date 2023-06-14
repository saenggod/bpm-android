package com.team.bpm.presentation.ui.main.lounge.community.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.usecase.community.*
import com.team.bpm.domain.usecase.user.GetUserIdUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.model.ReportType
import com.team.bpm.presentation.ui.main.lounge.question.detail.QuestionDetailContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getCommunityDetailUseCase: GetCommunityDetailUseCase,
    private val deleteCommunityUseCase: DeleteCommunityUseCase,
    private val reportCommunityUseCase: ReportCommunityUseCase,
    private val likeCommunityUseCase: LikeCommunityUseCase,
    private val dislikeCommunityUseCase: DislikeCommunityUseCase,
    private val writeCommunityCommentUseCase: WriteCommunityCommentUseCase,
    private val getCommunityCommentListUseCase: GetCommunityCommentListUseCase,
    private val deleteCommunityCommentUseCase: DeleteCommunityCommentUseCase,
    private val reportCommunityCommentUseCase: ReportCommunityCommentUseCase,
    private val likeCommunityCommentUseCase: LikeCommunityCommentUseCase,
    private val dislikeCommunityCommentUseCase: DislikeCommunityCommentUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModelV2(), CommunityDetailContract {

    private val _state = MutableStateFlow(CommunityDetailContract.State())
    override val state: StateFlow<CommunityDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CommunityDetailContract.Effect>()
    override val effect: SharedFlow<CommunityDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: CommunityDetailContract.Event) = when (event) {
        is CommunityDetailContract.Event.GetUserId -> {
            getUserId()
        }

        is CommunityDetailContract.Event.GetCommunityDetail -> {
            getCommunityDetail()
        }

        is CommunityDetailContract.Event.OnClickCommunityActionButton -> {
            onClickCommunityActionButton()
        }

        is CommunityDetailContract.Event.OnClickDeleteCommunity -> {
            onClickDeleteCommunity()
        }

        is CommunityDetailContract.Event.OnClickReportCommunity -> {
            onClickReportCommunity()
        }

        is CommunityDetailContract.Event.OnClickSendCommunityReport -> {
            onClickSendCommunityReport(event.reason)
        }

        is CommunityDetailContract.Event.OnClickLike -> {
            onClickLike()
        }

        is CommunityDetailContract.Event.OnClickSendComment -> {
            onClickSendComment(comment = event.comment)
        }

        is CommunityDetailContract.Event.GetCommentList -> {
            getCommentList()
        }

        is CommunityDetailContract.Event.OnClickCommentActionButton -> {
            onClickCommentActionButton(selectedComment = event.comment)
        }

        is CommunityDetailContract.Event.OnClickDeleteComment -> {
            onClickDeleteComment()
        }

        is CommunityDetailContract.Event.OnClickReportComment -> {
            onClickReportComment()
        }

        is CommunityDetailContract.Event.OnClickDismissReportDialog -> {
            onClickDismissReportDialog()
        }

        is CommunityDetailContract.Event.OnClickDismissNoticeDialog -> {
            onClickDismissNoticeDialog()
        }

        is CommunityDetailContract.Event.OnClickSendCommentReport -> {
            onClickSendCommentReport(event.reason)
        }

        is CommunityDetailContract.Event.OnClickCommentLike -> {
            onClickCommentLike(event.commentId)
        }

        is CommunityDetailContract.Event.OnClickDismissNoticeToQuitDialog -> {
            onClickDismissNoticeToQuitDialog()
        }

        is CommunityDetailContract.Event.OnBottomSheetHide -> {
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

    private fun getCommunityId(): Int? {
        return savedStateHandle.get<Int>(CommunityDetailActivity.KEY_COMMUNITY_ID)
    }

    private fun getCommunityDetail() {
        getCommunityId()?.let { communityId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    getCommunityDetailUseCase(communityId).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    community = result,
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

    private fun onClickCommunityActionButton() {
        state.value.community?.author?.id?.let { communityAuthorId ->
            viewModelScope.launch {
                _state.update {
                    val bottomSheetButtonList = mutableListOf<BottomSheetButton>().apply {
                        if (communityAuthorId == it.userId) {
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

    private fun onClickDeleteCommunity() {
        getCommunityId()?.let { communityId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        isLoading = true,
                        isBottomSheetShowing = false
                    )
                }

                withContext(ioDispatcher) {
                    deleteCommunityUseCase(communityId).onEach {
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

    private fun onClickReportCommunity() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    reportType = ReportType.POST,
                    isReportDialogShowing = true,
                    isBottomSheetShowing = false,
                    isReporting = true
                )
            }
        }
    }

    private fun onClickSendCommunityReport(reason: String) {
        getCommunityId()?.let { communityId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        isLoading = true,
                        isReportDialogShowing = false
                    )
                }

                withContext(ioDispatcher) {
                    reportCommunityUseCase(communityId, reason).onEach {
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
        getCommunityId()?.let { communityId ->
            viewModelScope.launch(ioDispatcher) {
                state.value.liked?.let {
                    when (it) {
                        true -> {
                            dislikeCommunityUseCase(communityId).onEach {
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
                            likeCommunityUseCase(communityId).onEach {
                                withContext(mainImmediateDispatcher) {
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
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

    private fun onClickSendComment(comment: String) {
        getCommunityId()?.let { communityId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    writeCommunityCommentUseCase(communityId, comment).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    commentIdToScroll = result.id,
                                    selectedComment = null
                                )
                            }

                            _effect.emit(CommunityDetailContract.Effect.RefreshCommentList)
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }

    private fun getCommentList() {
        getCommunityId()?.let { communityId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isCommentListLoading = true)
                }

                withContext(ioDispatcher) {
                    getCommunityCommentListUseCase(communityId).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            val commentList = mutableListOf<Comment>().apply {
                                result.comments?.filter { it.reported == false }?.forEach { comment ->
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

    private fun onClickCommentActionButton(selectedComment: Comment) {
        selectedComment.id?.let { commentId ->
            selectedComment.author?.id?.let { authorId ->
                viewModelScope.launch {
                    _state.update {
                        val bottomSheetButtonList = mutableListOf<BottomSheetButton>().apply {
                            if (authorId == it.userId) {
                                add(BottomSheetButton.DELETE_COMMENT)
                            } else {
                                add(BottomSheetButton.REPORT_COMMENT)
                            }
                        }

                        it.copy(
                            selectedComment = selectedComment,
                            commentIdToScroll = commentId,
                            bottomSheetButtonList = bottomSheetButtonList,
                            isBottomSheetShowing = true
                        )
                    }
                }
            }
        }
    }

    private fun onClickDeleteComment() {
        getCommunityId()?.let { communityId ->
            state.value.selectedComment?.id?.let { selectedCommentId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isCommentListLoading = true,
                            isBottomSheetShowing = false
                        )
                    }

                    withContext(ioDispatcher) {
                        deleteCommunityCommentUseCase(communityId, selectedCommentId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        isCommentListLoading = false,
                                        isNoticeDialogShowing = true,
                                        noticeDialogContent = "삭제가 완료되었습니다."
                                    )
                                }

                                _effect.emit(CommunityDetailContract.Effect.RefreshCommentList)
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
                it.copy(
                    reportType = ReportType.COMMENT,
                    isReportDialogShowing = true,
                    isBottomSheetShowing = false,
                    isReporting = true
                )
            }
        }
    }

    private fun onClickSendCommentReport(reason: String) {
        getCommunityId()?.let { communityId ->
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
                        reportCommunityCommentUseCase(communityId, selectedCommentId, reason).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        isCommentListLoading = false,
                                        isNoticeDialogShowing = true,
                                        noticeDialogContent = "신고가 완료되었습니다.",
                                    )
                                }

                                _effect.emit(CommunityDetailContract.Effect.RefreshCommentList)
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }
                }
            }
        }
    }

    private fun onClickCommentLike(commentId: Int) {
        getCommunityId()?.let { communityId ->
            val comment = state.value.commentList.find { comment -> comment.id == commentId }

            viewModelScope.launch(ioDispatcher) {
                when (comment?.liked) {
                    true -> {
                        dislikeCommunityCommentUseCase(communityId, commentId).onEach {
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
                        likeCommunityCommentUseCase(communityId, commentId).onEach {
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
                            _effect.emit(CommunityDetailContract.Effect.ShowToast("좋아요 기능을 사용할 수 없습니다."))
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
            _effect.emit(CommunityDetailContract.Effect.GoToCommunityList)
        }
    }

    private fun onBottomSheetHide() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isBottomSheetShowing = false,
                    selectedComment = if (it.isReporting) it.selectedComment else null
                )
            }
        }
    }
}