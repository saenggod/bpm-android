package com.team.bpm.presentation.ui.main.lounge

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
class LoungeViewModel @Inject constructor() : BaseViewModel(), LoungeContract {

    private val _state = MutableStateFlow(LoungeContract.State())
    override val state: StateFlow<LoungeContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoungeContract.Effect>()
    override val effect: SharedFlow<LoungeContract.Effect> = _effect.asSharedFlow()

    override fun event(event: LoungeContract.Event) {
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