package com.team.bpm.presentation.ui.schedule.select_studio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Studio
import com.team.bpm.domain.usecase.search_studio.SearchStudioUseCase
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
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectStudioViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val searchStudioUseCase: SearchStudioUseCase,
) : ViewModel(), SelectStudioContract {

    private val _state = MutableStateFlow(SelectStudioContract.State())
    override val state: StateFlow<SelectStudioContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SelectStudioContract.Effect>()
    override val effect: SharedFlow<SelectStudioContract.Effect> = _effect.asSharedFlow()

    override fun event(event: SelectStudioContract.Event) = when (event) {
        is SelectStudioContract.Event.OnClickSearch -> {
            getStudioList(event.query)
        }

        is SelectStudioContract.Event.OnClickStudio -> {
            onClickStudio(event.studio)
        }

        SelectStudioContract.Event.OnClickComplete -> {
            onClickComplete()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getStudioList(query: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            withContext(ioDispatcher) {
                searchStudioUseCase(query = query).onEach { result ->
                    withContext(mainImmediateDispatcher) {
                        when (result) {
                            is ResponseState.Success -> {
                                _state.update {
                                    it.copy(isLoading = false, studioList = result.data.studios ?: emptyList(), studioCount = result.data.studioCount ?: 0)
                                }
                            }

                            is ResponseState.Error -> {
                                // TODO : Show Error Dialog
                            }
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
            }
        }
    }

    private fun onClickStudio(studio: Studio) {
        viewModelScope.launch {
            _state.update {
                it.copy(selectedStudio = if (state.value.selectedStudio != studio) studio else null)
            }
        }
    }

    private fun onClickComplete() {
        state.value.selectedStudio?.let { studio ->
            viewModelScope.launch {
                _effect.emit(SelectStudioContract.Effect.Finish(studio))
            }
        }
    }
}