package com.team.bpm.presentation.ui.studio_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.Studio
import com.team.bpm.domain.usecase.review.GetReviewListUseCase
import com.team.bpm.domain.usecase.studio_detail.StudioDetailUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StudioDetailViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val studioDetailUseCase: StudioDetailUseCase,
    private val reviewListUseCase: GetReviewListUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), StudioDetailContract {

    private val _state = MutableStateFlow(StudioDetailContract.State())
    override val state: StateFlow<StudioDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<StudioDetailContract.Effect>()
    override val effect: SharedFlow<StudioDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: StudioDetailContract.Event) = when (event) {
        is StudioDetailContract.Event.GetStudioDetailData -> {
            getStudioId()?.let { getStudioDetailData(it) } ?: run {
                // TODO : Error Handling
            }
        }
        is StudioDetailContract.Event.ShowErrorDialog -> {
            showErrorDialog()
        }
        is StudioDetailContract.Event.OnClickQuit -> {
            onClickQuit()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getStudioId(): Int? {
        return savedStateHandle.get<Int>(StudioDetailActivity.KEY_STUDIO_ID)
    }

    private fun getStudioDetailData(studioId: Int) {
        _state.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            studioDetailUseCase(studioId).zip(reviewListUseCase(studioId)) { studioResult, reviewListResult ->
                Pair(studioResult, reviewListResult)
            }.onEach { pair ->
                withContext(mainImmediateDispatcher) {
                    if (pair.first is ResponseState.Error) {
                        _effect.emit(StudioDetailContract.Effect.LoadFailed)
                    } else {
                        _state.update {
                            it.copy(isLoading = false, studio = (pair.first as ResponseState.Success<Studio>).data)
                        }

                        if (pair.second is ResponseState.Success) {
                            _state.update {
                                it.copy(reviewList = (pair.second as ResponseState.Success<List<Review>>).data)
                            }
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun showErrorDialog() {
        _state.update {
            it.copy(isErrorDialogShowing = true)
        }
    }

    private fun onClickQuit() {
        _state.update {
            it.copy(isErrorDialogShowing = false)
        }

        viewModelScope.launch {
            _effect.emit(StudioDetailContract.Effect.Quit)
        }
    }
}