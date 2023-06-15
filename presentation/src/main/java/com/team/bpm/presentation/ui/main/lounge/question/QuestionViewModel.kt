package com.team.bpm.presentation.ui.main.lounge.question

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Error
import com.team.bpm.domain.model.Question
import com.team.bpm.domain.usecase.question.GetQuestionListUseCase
import com.team.bpm.presentation.base.BaseViewModel
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
class QuestionViewModel @Inject constructor(
    private val getQuestionListUseCase: GetQuestionListUseCase,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), QuestionContract {

    private val _state = MutableStateFlow(QuestionContract.State())
    override val state: StateFlow<QuestionContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<QuestionContract.Effect>()
    override val effect: SharedFlow<QuestionContract.Effect> = _effect.asSharedFlow()

    var offset: Int = 0

    override fun event(event: QuestionContract.Event) {
        when (event) {
            is QuestionContract.Event.OnClickListItem -> {
                goToQuestionDetail(event.questionId)
            }
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            val error = throwable as? Error
            viewModelScope.launch {
                _effect.emit(
                    QuestionContract.Effect.ShowToast(
                        error?.message ?: "오류가 발생했습니다. 다시 시도해주세요."
                    )
                )
            }
        }
    }

    fun getQuestionList(offset: Int = 0) {
        viewModelScope.launch(ioDispatcher) {
            getQuestionListUseCase(offset = offset, limit = 20, slug = null)
                .onEach { data ->
                    withContext(mainImmediateDispatcher) {
                        val dataList = arrayListOf<Question>()
                        _state.update {
                            dataList.addAll(data.questionBoardList ?: emptyList())
                            dataList.addAll(it.questionList ?: emptyList())
                            it.copy(
                                questionList = dataList.distinctBy { it.id }
                            )
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    fun goToQuestionDetail(questionId: Int) {
        viewModelScope.launch {
            _effect.emit(QuestionContract.Effect.GoToQuestionDetail(questionId))
        }
    }
}