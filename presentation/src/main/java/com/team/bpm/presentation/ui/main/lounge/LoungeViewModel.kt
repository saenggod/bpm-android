package com.team.bpm.presentation.ui.main.lounge

import androidx.lifecycle.viewModelScope
import com.team.bpm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoungeViewModel @Inject constructor() : BaseViewModel(), LoungeContract {

    private val _state = MutableStateFlow(LoungeContract.State())
    override val state: StateFlow<LoungeContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoungeContract.Effect>()
    override val effect: SharedFlow<LoungeContract.Effect> = _effect.asSharedFlow()

    override fun event(event: LoungeContract.Event) {
        when (event) {
            LoungeContract.Event.OnClickAdd -> {
                goToAddPost()
            }

            LoungeContract.Event.OnClickSearch -> {
                // TODO : 검색 붙이기!
            }

            LoungeContract.Event.OnClickAddCommunityPost -> {
                goToAddCommunityPost()
            }

            LoungeContract.Event.OnClickAddQuestionPost -> {
                goToAddQuestionPost()
            }
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            // TODO : Error Handling
        }
    }

    fun setCurrentTabPosition(position : Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    currentTabPosition = position
                )
            }
        }
    }

    fun onClickAdd() {
        viewModelScope.launch {
            event(LoungeContract.Event.OnClickAdd)
        }
    }

    fun onClickSearch() {
        viewModelScope.launch {
            event(LoungeContract.Event.OnClickSearch)
        }
    }

    fun onClickAddCommunityPost() {
        event(LoungeContract.Event.OnClickAddCommunityPost)
    }

    fun onClickAddQuestionPost() {
        event(LoungeContract.Event.OnClickAddQuestionPost)
    }

    private fun goToAddPost() {
        viewModelScope.launch {
            _effect.emit(LoungeContract.Effect.ShowAddBottomSheet)
        }
    }

    private fun goToAddCommunityPost() {
        viewModelScope.launch {
            _effect.emit(LoungeContract.Effect.GoToAddCommunityPost)
        }
    }

    private fun goToAddQuestionPost() {
        viewModelScope.launch {
            _effect.emit(LoungeContract.Effect.GoToAddQuestionPost)
        }
    }
}