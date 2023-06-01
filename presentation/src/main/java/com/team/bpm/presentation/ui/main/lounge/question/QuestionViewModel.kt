package com.team.bpm.presentation.ui.main.lounge.question

import com.team.bpm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor() : BaseViewModel(), QuestionContract {

    private val _state = MutableStateFlow(QuestionContract.State())
    override val state: StateFlow<QuestionContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<QuestionContract.Effect>()
    override val effect: SharedFlow<QuestionContract.Effect> = _effect.asSharedFlow()

    override fun event(event: QuestionContract.Event) {
//        when (event) {
//
//        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            // TODO : Error Handling
        }
    }

}