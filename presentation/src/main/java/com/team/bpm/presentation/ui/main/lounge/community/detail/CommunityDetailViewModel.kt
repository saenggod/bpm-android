package com.team.bpm.presentation.ui.main.lounge.community.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.usecase.community.*
import com.team.bpm.domain.usecase.splash.GetKakaoIdUseCase
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
class CommunityDetailViewModel @Inject constructor(
    private val getCommunityDetailUseCase: GetCommunityDetailUseCase,
    private val getCommunityCommentListUseCase: GetCommunityCommentListUseCase,
    private val deleteCommunityUseCase: DeleteCommunityUseCase,
    private val reportCommunityUseCase: ReportCommunityUseCase,
    private val writeCommunityCommentUseCase: WriteCommunityCommentUseCase,
    private val likeCommunityUseCase: LikeCommunityUseCase,
    private val dislikeCommunityUseCase: DislikeCommunityUseCase,
    private val deleteCommunityCommentUseCase: DeleteCommunityCommentUseCase,
    private val reportCommunityCommentUseCase: ReportCommunityCommentUseCase,
    private val likeCommunityCommentUseCase: LikeCommunityCommentUseCase,
    private val dislikeCommunityCommentUseCase: DislikeCommunityCommentUseCase,
    private val getKakaoIdUseCase: GetKakaoIdUseCase,
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

        is CommunityDetailContract.Event.GetCommentList -> {
            getCommentList()
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

        is CommunityDetailContract.Event.OnClickSendComment -> {
            onClickSendComment(comment = event.comment)
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

        is CommunityDetailContract.Event.OnClickLike -> {
            onClickLike()
        }

        is CommunityDetailContract.Event.OnClickCommentLike -> {
            onClickCommentLike(event.commentId)
        }

        is CommunityDetailContract.Event.OnClickBackButton -> {
            onClickBackButton()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getUserId() {
        viewModelScope.launch(ioDispatcher) {
            getKakaoIdUseCase().onEach { result ->
                result?.let { userId ->
                    withContext(mainImmediateDispatcher) {

                    }
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    private fun getCommunityId(): Int? {
        return savedStateHandle.get<Int>(CommunityDetailActivity.KEY_QUESTION_ID)
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
                                    liked = result.favorite,
                                    likeCount = result.favoriteCount
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
                        if (communityAuthorId == state.value.userId) {
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
                            _effect.emit(CommunityDetailContract.Effect.GoToCommunityList)
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
                    isBottomSheetShowing = false
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

    private fun onClickLike() {
        getCommunityId()?.let { communityId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    state.value.liked?.let {
                        when (it) {
                            true -> {
                                dislikeCommunityUseCase(communityId).onEach {
                                    withContext(mainImmediateDispatcher) {
                                        _state.update {
                                            it.copy(
                                                isLoading = false,
                                                liked = false,
                                                likeCount = state.value.likeCount?.minus(1)
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
                                                likeCount = state.value.likeCount?.plus(1)
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

    private fun onClickSendComment(comment: String) {
        getCommunityId()?.let { communityId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    writeCommunityCommentUseCase(communityId = communityId, parentId = null, comment = comment).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    redirectCommentId = result.id,
                                    selectedCommentId = null,
                                    selectedCommentAuthorId = null
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

    private fun onClickCommentActionButton(selectedComment: Comment) {
        selectedComment.id?.let { commentId ->
            selectedComment.author?.id?.let { authorId ->
                viewModelScope.launch {
                    _state.update {
                        val bottomSheetButtonList = mutableListOf<BottomSheetButton>().apply {
                            if (authorId == state.value.userId) {
                                add(BottomSheetButton.DELETE_COMMENT)
                            } else {
                                add(BottomSheetButton.REPORT_COMMENT)
                            }
                        }

                        it.copy(
                            selectedCommentId = commentId,
                            selectedCommentAuthorId = authorId,
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
            state.value.selectedCommentId?.let { selectedCommentId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(isCommentListLoading = true)
                    }

                    withContext(ioDispatcher) {
                        deleteCommunityCommentUseCase(communityId, selectedCommentId).onEach {
                            withContext(mainImmediateDispatcher) {
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
                    isBottomSheetShowing = false
                )
            }
        }
    }

    private fun onClickSendCommentReport(reason: String) {
        getCommunityId()?.let { communityId ->
            state.value.selectedCommentId?.let { selectedCommentId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isCommentListLoading = true,
                            isReportDialogShowing = false
                        )
                    }

                    withContext(ioDispatcher) {
                        reportCommunityCommentUseCase(communityId, selectedCommentId, reason).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(isCommentListLoading = false)
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
                        likeCommunityCommentUseCase(communityId, commentId).onEach {
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

    private fun onClickBackButton() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isBottomSheetShowing = false,
                    selectedCommentId = null,
                    selectedCommentAuthorId = null
                )
            }
        }
    }
}