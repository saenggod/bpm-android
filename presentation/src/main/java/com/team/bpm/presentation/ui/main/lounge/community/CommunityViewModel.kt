package com.team.bpm.presentation.ui.main.lounge.community

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
class CommunityViewModel @Inject constructor() : BaseViewModel(), CommunityContract {

    private val _state = MutableStateFlow(CommunityContract.State())
    override val state: StateFlow<CommunityContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CommunityContract.Effect>()
    override val effect: SharedFlow<CommunityContract.Effect> = _effect.asSharedFlow()

    override fun event(event: CommunityContract.Event) {
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