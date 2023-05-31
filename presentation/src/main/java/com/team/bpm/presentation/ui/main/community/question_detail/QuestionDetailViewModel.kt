package com.team.bpm.presentation.ui.main.community.question_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.usecase.question.GetQuestionCommentListUseCase
import com.team.bpm.domain.usecase.question.GetQuestionDetailUseCase
import com.team.bpm.domain.usecase.question.SendQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.DislikeQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.DislikeQuestionUseCase
import com.team.bpm.domain.usecase.question.LikeQuestionCommentUseCase
import com.team.bpm.domain.usecase.question.LikeQuestionUseCase
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
class QuestionDetailViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getQuestionDetailUseCase: GetQuestionDetailUseCase,
    private val getQuestionCommentListUseCase: GetQuestionCommentListUseCase,
    private val sendQuestionCommentUseCase: SendQuestionCommentUseCase,
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
        getQuestionId()?.let { questionId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    getQuestionDetailUseCase(questionId).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(isLoading = false, question = result, liked = result.favorited, likeCount = result.favoritesCount)
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }

    private fun getCommentList() {
        getQuestionId()?.let { questionId ->
            viewModelScope.launch(ioDispatcher) {
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
                            it.copy(commentList = commentList, commentsCount = result.commentsCount ?: result.comments?.size)
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
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
                    sendQuestionCommentUseCase(questionId = questionId, parentId = parentId, comment = comment).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(isLoading = false, redirectCommentId = result.id, selectedCommentId = null, parentCommentId = null)
                            }

                            _effect.emit(QuestionDetailContract.Effect.RefreshCommentList)
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
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
                                            it.copy(isLoading = false, liked = false, likeCount = state.value.likeCount?.minus(1))
                                        }

                                        _effect.emit(QuestionDetailContract.Effect.ShowToast("질문 추천을 취소하였습니다."))
                                    }
                                }.launchIn(viewModelScope + exceptionHandler)
                            }

                            false -> {
                                likeQuestionUseCase(questionId).onEach {
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
    }

    private fun onClickCommentLike(commentId: Int) {
        getQuestionId()?.let { questionId ->
            val comment = state.value.commentList?.find { comment -> comment.id == commentId }

            viewModelScope.launch(ioDispatcher) {
                when (comment?.liked) {
                    true -> {
                        dislikeQuestionCommentUseCase(questionId, commentId).onEach {
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
                        likeQuestionCommentUseCase(questionId, commentId).onEach {
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
}