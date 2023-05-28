package com.team.bpm.presentation.ui.main.community.question_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.question.GetCommentListUseCase
import com.team.bpm.domain.usecase.question.GetQuestionDetailUseCase
import com.team.bpm.domain.usecase.question.SendCommentUseCase
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
                        it.copy(isLoading = false, question = result)
                    }
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    private fun getCommentList() {
        viewModelScope.launch(ioDispatcher) {
            getCommentListUseCase(1).onEach { result ->
                withContext(mainImmediateDispatcher) {
                    _state.update {
                        it.copy(commentList = result.comments, commentsCount = result.commentsCount ?: result.comments?.size)
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
}