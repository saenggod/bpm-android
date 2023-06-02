package com.team.bpm.presentation.ui.main.lounge.add

import androidx.lifecycle.viewModelScope
import com.team.bpm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoungeAddViewModel @Inject constructor() : BaseViewModel(), LoungeAddContract {

    private val _state = MutableStateFlow(LoungeAddContract.State())
    override val state: StateFlow<LoungeAddContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoungeAddContract.Effect>()
    override val effect: SharedFlow<LoungeAddContract.Effect> = _effect.asSharedFlow()

    override fun event(event: LoungeAddContract.Event) {
        when (event) {
            LoungeAddContract.Event.OnClickAddCommunityPost -> {
                goToAddCommunityPost()
            }
            LoungeAddContract.Event.OnClickAddQuestionPost -> {
                goToAddQuestionPost()
            }
        }
    }

    fun onClickAddCommunityPost() {
        event(LoungeAddContract.Event.OnClickAddCommunityPost)
    }

    fun onClickAddQuestionPost() {
        event(LoungeAddContract.Event.OnClickAddQuestionPost)
    }

    private fun goToAddCommunityPost() {
        viewModelScope.launch {
            _effect.emit(LoungeAddContract.Effect.GoToAddCommunityPost)
        }
    }

    private fun goToAddQuestionPost() {
        viewModelScope.launch {
            _effect.emit(LoungeAddContract.Effect.GoToAddQuestionPost)
        }
    }
}