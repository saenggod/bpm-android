package com.team.bpm.presentation.ui.main.lounge.question

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Error
import com.team.bpm.domain.model.Question
import com.team.bpm.domain.usecase.question.GetQuestionListUseCase
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
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
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestionListUseCase: GetQuestionListUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), QuestionContract {

    private val _state = MutableStateFlow(QuestionContract.State())
    override val state: StateFlow<QuestionContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<QuestionContract.Effect>()
    override val effect: SharedFlow<QuestionContract.Effect> = _effect.asSharedFlow()

    var offset: Int = 0

    // 게시물 리프레시시 상단으로 보낼 지 결정하는 변수. default false
    var isFromDetail = false

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

    // TODO : 무한스크롤 (차기 버전에서 적용)
    fun getQuestionList(offset: Int = 0) {
        viewModelScope.launch(ioDispatcher) {
            getQuestionListUseCase(offset = offset, limit = 200, slug = null)
                .onEach { data ->
                    val dataList = arrayListOf<Question>()
                    _state.update {
                        dataList.addAll(data.questionBoardList ?: emptyList())
                        dataList.addAll(it.questionList ?: emptyList())
                        it.copy(
                            questionList = dataList.distinctBy { it.id }
                        )
                    }

                    if (!isFromDetail) {
                        delay(100)
                        _effect.emit(QuestionContract.Effect.GoToTop)
                    } else {
                        isFromDetail = false
                    }
                }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    fun goToQuestionDetail(questionId: Int) {
        isFromDetail = true
        viewModelScope.launch {
            _effect.emit(QuestionContract.Effect.GoToQuestionDetail(questionId))
        }
    }
}