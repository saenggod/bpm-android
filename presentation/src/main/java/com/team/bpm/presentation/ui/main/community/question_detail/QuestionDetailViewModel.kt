package com.team.bpm.presentation.ui.main.community.question_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.usecase.question.GetCommentListUseCase
import com.team.bpm.domain.usecase.question.GetQuestionDetailUseCase
import com.team.bpm.domain.usecase.question.SendCommentUseCase
import com.team.bpm.domain.usecase.question.like.DislikeQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.like.DislikeQuestionUseCase
import com.team.bpm.domain.usecase.question.like.LikeQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.like.LikeQuestionUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class QuestionDetailViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getQuestionDetailUseCase: GetQuestionDetailUseCase,
    private val getCommentListUseCase: GetCommentListUseCase,
    private val sendCommentUseCase: SendCommentUseCase,
    private val likeQuestionUseCase: LikeQuestionUseCase,
    private val dislikeQuestionUseCase: DislikeQuestionUseCase,
    private val likeQuestionCommentUseCase: LikeQuestionCommentUseCase,
    private val dislikeQuestionCommentUseCase: DislikeQuestionCommentUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), QuestionDetailContract {

    private val _state = MutableStateFlow(QuestionDetailContract.State())
    override val state: StateFlow<QuestionDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<QuestionDetailContract.Effect>()
    override val effect: SharedFlow<QuestionDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: QuestionDetailContract.Event) = when (event) {
        is QuestionDetailContract.Event.GetQuestionDetail -> {
            getQuestionDetail()
        }
        is QuestionDetailContract.Event.GetCommentList -> {
            getCommentList()
        }
        is QuestionDetailContract.Event.OnClickSendComment -> {
            onClickSendComment(parentId = event.parentId, comment = event.comment)
        }
        is QuestionDetailContract.Event.OnClickCommentActionButton -> {
            onClickCommentActionButton(event.commentId)
        }
        is QuestionDetailContract.Event.OnClickWriteCommentOnComment -> {
            onClickWriteCommentOnComment()
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
        return savedStateHandle.get<Int>(QuestionDetailActivity.KEY_QUESTION_ID)
    }

    private fun getQuestionDetail() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
        }

        viewModelScope.launch(ioDispatcher) {
            getQuestionDetailUseCase(1).onEach { result ->
                withContext(mainImmediateDispatcher) {
                    _state.update {
                        it.copy(isLoading = false, question = result, liked = result.favorited, likeCount = result.favoritesCount)
                    }
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    private fun getCommentList() {
        viewModelScope.launch(ioDispatcher) {
            getCommentListUseCase(1).onEach { result ->
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

    private fun onClickSendComment(parentId: Int?, comment: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
        }

        viewModelScope.launch(ioDispatcher) {
            sendCommentUseCase(questionId = 1, parentId = parentId, comment = comment).onEach { result ->
                withContext(mainImmediateDispatcher) {
                    _state.update {
                        it.copy(isLoading = false, redirectCommentId = result.id, selectedCommentId = null, parentCommentId = null)
                    }

                    _effect.emit(QuestionDetailContract.Effect.RefreshCommentList)
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    private fun onClickCommentActionButton(commentId: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(selectedCommentId = commentId)
            }

            _effect.emit(QuestionDetailContract.Effect.ExpandBottomSheet)
        }
    }

    private fun onClickWriteCommentOnComment() {
        viewModelScope.launch {
            _state.update {
                it.copy(parentCommentId = state.value.selectedCommentId)
            }

            _effect.emit(QuestionDetailContract.Effect.ShowKeyboard)
        }
    }

    private fun onClickLike() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            withContext(ioDispatcher) {
                state.value.liked?.let {
                    when (it) {
                        true -> {
                            dislikeQuestionUseCase(1).onEach {
                                withContext(mainImmediateDispatcher) {
                                    _state.update {
                                        it.copy(isLoading = false, liked = false, likeCount = state.value.likeCount?.minus(1))
                                    }

                                    _effect.emit(QuestionDetailContract.Effect.ShowToast("질문 추천을 취소하였습니다."))
                                }
                            }.launchIn(viewModelScope + exceptionHandler)
                        }

                        false -> {
                            likeQuestionUseCase(1).onEach {
                                withContext(mainImmediateDispatcher) {
                                    _state.update {
                                        it.copy(isLoading = false, liked = true, likeCount = state.value.likeCount?.plus(1))
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

    private fun onClickCommentLike(commentId: Int) {
        val comment = state.value.commentList?.find { comment -> comment.id == commentId }

        viewModelScope.launch(ioDispatcher) {
            when (comment?.liked) {
                true -> {
                    dislikeQuestionCommentUseCase(1, commentId).onEach {
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
                    likeQuestionCommentUseCase(1, commentId).onEach {
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
                        _effect.emit(QuestionDetailContract.Effect.ShowToast("좋아요 기능을 사용할 수 없습니다."))
                    }
                }
            }
        }
    }
}