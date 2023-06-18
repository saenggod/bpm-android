package com.team.bpm.presentation.ui.main.studio.schedule.select

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Studio
import com.team.bpm.domain.usecase.search_studio.SearchStudioUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectStudioViewModel @Inject constructor(private val searchStudioUseCase: SearchStudioUseCase) : BaseViewModelV2(),
    SelectStudioContract {

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

        is SelectStudioContract.Event.OnClickComplete -> {
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
                        result.studios?.let { studios ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    studioList = studios,
                                    studioCount = result.studioCount ?: studios.size
                                )
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
                it.copy(selectedStudio = if (it.selectedStudio != studio) studio else null)
            }
        }
    }

    private fun onClickComplete() {
        state.value.selectedStudio?.name?.let { studioName ->
            viewModelScope.launch {
                _effect.emit(SelectStudioContract.Effect.Finish(studioName))
            }
        }
    }
}