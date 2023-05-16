package com.team.bpm.presentation.ui.main.community.community_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.usecase.post.GetPostDetailUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), CommunityDetailContract {
    private val _state = MutableStateFlow(CommunityDetailContract.State())
    override val state: StateFlow<CommunityDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CommunityDetailContract.Effect>()
    override val effect: SharedFlow<CommunityDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: CommunityDetailContract.Event) = when (event) {
        CommunityDetailContract.Event.GetCommunityDetail -> {
            getCommunityDetail()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getPostId(): Int? {
        return savedStateHandle.get<Int>(CommunityDetailActivity.KEY_POST_ID)
    }

    private fun getCommunityDetail() {
        getPostId()?.let { postId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher + exceptionHandler) {
                    getPostDetailUseCase(postId).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            when (result) {
                                is ResponseState.Success -> {
                                    _state.update {
                                        it.copy(isLoading = false, post = result.data)
                                    }
                                }

                                is ResponseState.Error -> {
                                    // TODO : Show error dialog
                                }
                            }
                        }
                    }.launchIn(viewModelScope)
                }
            }
        }
    }
}