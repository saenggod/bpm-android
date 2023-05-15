package com.team.bpm.presentation.ui.main.community.community_detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(

) : ViewModel(), CommunityDetailContract {
    private val _state = MutableStateFlow(CommunityDetailContract.State())
    override val state: StateFlow<CommunityDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CommunityDetailContract.Effect>()
    override val effect: SharedFlow<CommunityDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: CommunityDetailContract.Event) {
        TODO("Not yet implemented")
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

}