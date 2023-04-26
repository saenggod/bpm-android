package com.team.bpm.presentation.ui.main.community.community_posting

import androidx.lifecycle.ViewModel
import com.team.bpm.presentation.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CommunityPostingViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), CommunityPostingContract {
    private val _state = MutableStateFlow(CommunityPostingContract.State())
    override val state: StateFlow<CommunityPostingContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CommunityPostingContract.Effect>()
    override val effect: SharedFlow<CommunityPostingContract.Effect> = _effect.asSharedFlow()

    override fun event(event: CommunityPostingContract.Event) = when (event) {
        else -> {

        }
    }
}