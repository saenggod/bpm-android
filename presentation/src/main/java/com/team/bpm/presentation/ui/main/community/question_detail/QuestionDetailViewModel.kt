package com.team.bpm.presentation.ui.main.community.question_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseStateV2
import com.team.bpm.domain.usecase.question.GetQuestionDetailUseCase
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
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            println(throwable)
        }
    }

    private fun getQuestionId(): Int? {
        return savedStateHandle.get<Int>(QuestionDetailActivity.KEY_QUESTION_ID)
    }

    private fun getQuestionDetail() {
        viewModelScope.launch(ioDispatcher) {
            getQuestionDetailUseCase(1).onEach { result ->
                withContext(mainImmediateDispatcher) {
                    _state.update {
                        when (result) {
                            is ResponseStateV2.Success -> it.copy(isLoading = false, question = result.data)
                            is ResponseStateV2.Error -> it.copy(isLoading = false)
                        }
                    }
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }
}