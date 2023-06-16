package com.team.bpm.presentation.ui.main.mypage.myquestion.more

import androidx.lifecycle.viewModelScope
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyQuestionMoreViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
) : BaseViewModel(), MyQuestionMoreContract {

    private val _state = MutableStateFlow(MyQuestionMoreContract.State())
    override val state: StateFlow<MyQuestionMoreContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MyQuestionMoreContract.Effect>()
    override val effect: SharedFlow<MyQuestionMoreContract.Effect> = _effect.asSharedFlow()

    override fun event(event: MyQuestionMoreContract.Event) {
        viewModelScope.launch(mainImmediateDispatcher) {
            when (event) {
                MyQuestionMoreContract.Event.Delete -> {
                    _effect.emit(MyQuestionMoreContract.Effect.GoDelete)
                }
            }
        }
    }

    fun clickDelete() {
        event(MyQuestionMoreContract.Event.Delete)
    }
}