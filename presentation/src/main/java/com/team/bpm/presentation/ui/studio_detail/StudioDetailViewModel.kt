package com.team.bpm.presentation.ui.studio_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.usecase.review.GetReviewListUseCase
import com.team.bpm.domain.usecase.studio_detail.StudioDetailUseCase
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StudioDetailViewModel @Inject constructor(
    private val studioDetailUseCase: StudioDetailUseCase,
    private val reviewListUseCase: GetReviewListUseCase,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val eventChannel = Channel<StudioDetailEvent>()
    private val _sideEffectChannel = Channel<StudioDetailSideEffect>()
    private val sideEffectChannel = _sideEffectChannel.receiveAsFlow()

    val state: StateFlow<StudioDetailState> = eventChannel.receiveAsFlow()
        .runningFold(StudioDetailState(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StudioDetailState())

    private fun reduceState(state: StudioDetailState, event: StudioDetailEvent): StudioDetailState {
        return when (event) {
            is StudioDetailEvent.LoadStudioDetailInfo -> state.copy(isLoading = true)
            is StudioDetailEvent.OnLoadedStudioDetailInfo -> state.copy(isLoading = false, studio = event.studio, reviews = event.reviews)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    init {
        getStudioDetailInfo(getStudioId() ?: 0)
    }

    private fun getStudioId(): Int? {
        return savedStateHandle.get<Int>(StudioDetailActivity.KEY_STUDIO_ID)
    }

    private fun getStudioDetailInfo(studioId: Int) {
        viewModelScope.launch(mainImmediateDispatcher) {
            eventChannel.send(StudioDetailEvent.LoadStudioDetailInfo)
        }

        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            studioDetailUseCase(studioId).zip(reviewListUseCase(studioId)) { infoResult, reviewResult ->
                if (infoResult is ResponseState.Success &&
                    reviewResult is ResponseState.Success
                ) {
                    withContext(mainImmediateDispatcher) {
                        eventChannel.send(
                            StudioDetailEvent.OnLoadedStudioDetailInfo(
                                studio = infoResult.data,
                                reviews = reviewResult.data
                            )
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onClickCall() {
        viewModelScope.launch(mainImmediateDispatcher) {

        }
    }

    fun onClickInfoEditSuggestion() {
        viewModelScope.launch(mainImmediateDispatcher) {

        }
    }

    fun onClickMap() {
        viewModelScope.launch(mainImmediateDispatcher) {

        }
    }

    fun onClickCopyAddress() {
        viewModelScope.launch(mainImmediateDispatcher) {

        }
    }

    fun onClickShowCourse() {
        viewModelScope.launch(mainImmediateDispatcher) {

        }
    }

    fun onClickWriteReview() {
        viewModelScope.launch(mainImmediateDispatcher) {

        }
    }

    fun onClickLike() {
        viewModelScope.launch(mainImmediateDispatcher) {

        }
    }

    fun onClickMoreReview() {
        viewModelScope.launch(mainImmediateDispatcher) {

        }
    }
}