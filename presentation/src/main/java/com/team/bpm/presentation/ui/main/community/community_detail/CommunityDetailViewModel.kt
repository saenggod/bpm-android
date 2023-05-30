package com.team.bpm.presentation.ui.main.community.community_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.usecase.community.GetCommunityCommentListUseCase
import com.team.bpm.domain.usecase.community.GetCommunityDetailUseCase
import com.team.bpm.domain.usecase.community.SendCommunityCommentUseCase
import com.team.bpm.domain.usecase.community.like.DislikeCommunityCommentUseCase
import com.team.bpm.domain.usecase.community.like.DislikeCommunityUseCase
import com.team.bpm.domain.usecase.community.like.LikeCommunityCommentUseCase
import com.team.bpm.domain.usecase.community.like.LikeCommunityUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getCommunityDetailUseCase: GetCommunityDetailUseCase,
    private val getCommunityCommentListUseCase: GetCommunityCommentListUseCase,
    private val sendCommunityCommentUseCase: SendCommunityCommentUseCase,
    private val likeCommunityUseCase: LikeCommunityUseCase,
    private val dislikeCommunityUseCase: DislikeCommunityUseCase,
    private val likeCommunityCommentUseCase: LikeCommunityCommentUseCase,
    private val dislikeCommunityCommentUseCase: DislikeCommunityCommentUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), CommunityDetailContract {

    private val _state = MutableStateFlow(CommunityDetailContract.State())
    override val state: StateFlow<CommunityDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CommunityDetailContract.Effect>()
    override val effect: SharedFlow<CommunityDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: CommunityDetailContract.Event) = when (event) {
        is CommunityDetailContract.Event.GetCommunityDetail -> {
            getCommunityDetail()
        }
        is CommunityDetailContract.Event.GetCommentList -> {
            getCommentList()
        }
        is CommunityDetailContract.Event.OnClickSendComment -> {
            onClickSendComment(comment = event.comment)
        }
        is CommunityDetailContract.Event.OnClickCommentActionButton -> {
            onClickCommentActionButton(event.commentId)
        }
        is CommunityDetailContract.Event.OnClickLike -> {
            onClickLike()
        }
        is CommunityDetailContract.Event.OnClickCommentLike -> {
            onClickCommentLike(event.commentId)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

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
            }

            viewModelScope.launch(ioDispatcher) {
                getCommunityDetailUseCase(communityId).onEach { result ->
                    withContext(mainImmediateDispatcher) {
                        _state.update {
                            it.copy(isLoading = false, community = result, liked = result.liked, likeCount = result.likeCount)
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
            }
        }
    }

    private fun getCommentList() {
        getCommunityId()?.let { communityId ->
            viewModelScope.launch(ioDispatcher) {
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
                            it.copy(commentList = commentList, commentsCount = result.commentsCount ?: result.comments?.size)
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
            }
        }
    }

    private fun onClickSendComment(comment: String) {
        getCommunityId()?.let { communityId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }
            }

            viewModelScope.launch(ioDispatcher) {
                sendCommunityCommentUseCase(communityId = communityId, parentId = null, comment = comment).onEach { result ->
                    withContext(mainImmediateDispatcher) {
                        _state.update {
                            it.copy(isLoading = false, redirectCommentId = result.id, selectedCommentId = null, parentCommentId = null)
                        }

                        _effect.emit(CommunityDetailContract.Effect.RefreshCommentList)
                    }
                }.launchIn(viewModelScope + exceptionHandler)
            }
        }
    }

    private fun onClickCommentActionButton(commentId: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(selectedCommentId = commentId)
            }

            _effect.emit(CommunityDetailContract.Effect.ExpandBottomSheet)
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
                                            it.copy(isLoading = false, liked = false, likeCount = state.value.likeCount?.minus(1))
                                        }

                                        _effect.emit(CommunityDetailContract.Effect.ShowToast("게시글 추천을 취소하였습니다."))
                                    }
                                }.launchIn(viewModelScope + exceptionHandler)
                            }

                            false -> {
                                likeCommunityUseCase(communityId).onEach {
                                    withContext(mainImmediateDispatcher) {
                                        _state.update {
                                            it.copy(isLoading = false, liked = true, likeCount = state.value.likeCount?.plus(1))
                                        }

                                        _effect.emit(CommunityDetailContract.Effect.ShowToast("게시글을 추천하였습니다."))
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
        getCommunityId()?.let { communityId ->
            val comment = state.value.commentList?.find { comment -> comment.id == commentId }

            viewModelScope.launch(ioDispatcher) {
                when (comment?.liked) {
                    true -> {
                        dislikeCommunityCommentUseCase(communityId, commentId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(commentList = state.value.commentList?.toMutableList()?.apply {
                                        val targetIndex = indexOf(comment)
                                        this[targetIndex] = this[targetIndex].copy(liked = false, likeCount = this[targetIndex].likeCount?.minus(1))
                                    })
                                }
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }

                    false -> {
                        likeCommunityCommentUseCase(communityId, commentId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(commentList = state.value.commentList?.toMutableList()?.apply {
                                        val targetIndex = indexOf(comment)
                                        this[targetIndex] = this[targetIndex].copy(liked = true, likeCount = this[targetIndex].likeCount?.plus(1))
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
}