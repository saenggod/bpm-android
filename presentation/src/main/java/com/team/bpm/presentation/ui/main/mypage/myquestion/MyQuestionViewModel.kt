package com.team.bpm.presentation.ui.main.mypage.myquestion

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Error
import com.team.bpm.domain.usecase.question.DeleteQuestionUseCase
import com.team.bpm.domain.usecase.question.GetMyQuestionListUseCase
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyQuestionViewModel @Inject constructor(
    private val getMyQuestionListUseCase: GetMyQuestionListUseCase,
    private val deleteQuestionUseCase: DeleteQuestionUseCase,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), MyQuestionContract {

    private val _state = MutableStateFlow(MyQuestionContract.State())
    override val state: StateFlow<MyQuestionContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MyQuestionContract.Effect>()
    override val effect: SharedFlow<MyQuestionContract.Effect> = _effect.asSharedFlow()

    var offset: Int = 0

    override fun event(event: MyQuestionContract.Event) {
        when (event) {
            is MyQuestionContract.Event.OnClickListItem -> {
                if (state.value.isDeleteMode) {
                    selectMyQuestion(event.questionId)
                } else {
                    goToQuestionDetail(event.questionId)
                }
            }

            MyQuestionContract.Event.OnClickMore -> {
                goToMoreBottomSheet()
            }

            MyQuestionContract.Event.OnClickDeleteMode -> {
                setupDeleteMode(true)
            }

            MyQuestionContract.Event.OnClickItemDelete -> {
                deleteMyQuestion()
            }

            MyQuestionContract.Event.OnBackPressed -> {
                handleBackPressed()
            }
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            val error = throwable as? Error
            viewModelScope.launch {
                _effect.emit(
                    MyQuestionContract.Effect.ShowToast(
                        error?.message ?: "오류가 발생했습니다. 다시 시도해주세요."
                    )
                )
            }
        }
    }

    fun getQuestionList(offset: Int = 0) {
        viewModelScope.launch(ioDispatcher) {
            getMyQuestionListUseCase(page = offset, size = 20)
                .onEach { data ->
                    withContext(mainImmediateDispatcher) {
                        _state.update {
                            it.copy(
                                questionList = data.questionBoardList
                            )
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    private fun deleteMyQuestion() {
        viewModelScope.launch(ioDispatcher) {
            val questionIdList =
                state.value.questionList?.filter { it.isSelected }?.mapNotNull { it.id } ?: emptyList()

            for (id in questionIdList) {
                deleteQuestionUseCase(questionId = id)
                    .launchIn(viewModelScope + exceptionHandler)
            }

            _effect.emit(MyQuestionContract.Effect.ShowToast("삭제가 완료되었습니다."))
            setupDeleteMode(false)
            delay(100)
            getQuestionList()
        }
    }

    private fun goToMoreBottomSheet() {
        viewModelScope.launch {
            _effect.emit(MyQuestionContract.Effect.ShowDeleteBottomSheet)
        }
    }

    private fun goToQuestionDetail(questionId: Int) {
        viewModelScope.launch {
            _effect.emit(MyQuestionContract.Effect.GoToQuestionDetail(questionId))
        }
    }

    private fun selectMyQuestion(questionId: Int) {
        viewModelScope.launch(mainImmediateDispatcher) {
            val list = state.value.questionList ?: emptyList()
            list.first { it.id == questionId }.isSelected =
                !list.first { it.id == questionId }.isSelected

            _state.update {
                it.copy(
                    isAllSelected = list.all { it.isSelected },
                    questionList = list
                )
            }
        }
    }

    fun onClickMore() {
        event(MyQuestionContract.Event.OnClickMore)
    }

    fun onClickDeleteMode() {
        event(MyQuestionContract.Event.OnClickDeleteMode)
    }

    fun onClickSelectAllItem() {
        viewModelScope.launch(mainImmediateDispatcher) {
            _state.update {
                val list = state.value.questionList ?: emptyList()
                list.forEach { it.isSelected = !it.isSelected }

                it.copy(
                    isAllSelected = list.all { it.isSelected },
                    questionList = list.toMutableList()
                )
            }
        }
    }

    fun onClickDeleteItem() {
        event(MyQuestionContract.Event.OnClickItemDelete)
    }

    private fun setupDeleteMode(isDeleteMode: Boolean) {
        viewModelScope.launch(mainImmediateDispatcher) {
            _state.update {
                val list = state.value.questionList ?: emptyList()
                list.forEach { it.isSelected = false }

                it.copy(
                    isDeleteMode = isDeleteMode,
                    questionList = list.toMutableList()
                )
            }
        }
    }

    private fun handleBackPressed() {
        viewModelScope.launch {
            if (state.value.isDeleteMode) {
                setupDeleteMode(false)
            } else {
                _effect.emit(MyQuestionContract.Effect.GoOut)
            }
        }
    }
}