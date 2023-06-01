package com.team.bpm.presentation.ui.main.lounge.add

import com.team.bpm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoungeAddViewModel @Inject constructor() : BaseViewModel(), LoungeAddContract {

    private val _state = MutableStateFlow(LoungeAddContract.State())
    override val state: StateFlow<LoungeAddContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoungeAddContract.Effect>()
    override val effect: SharedFlow<LoungeAddContract.Effect> = _effect.asSharedFlow()

    override fun event(event: LoungeAddContract.Event) {
        TODO("Not yet implemented")
    }

    fun onClickAddCommunityPost(){
        event(LoungeAddContract.Event.OnClickAddCommunityPost)
    }

    fun onClickAddQuestionPost(){
        event(LoungeAddContract.Event.OnClickAddQuestionPost)
    }
}