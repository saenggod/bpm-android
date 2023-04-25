package com.team.bpm.presentation.ui.main.studio.recommend

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Studio
import com.team.bpm.domain.usecase.main.GetStudioListUseCase
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class StudioHomeRecommendViewModel @Inject constructor(
    private val getStudioListUseCase: GetStudioListUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _state = MutableStateFlow<StudioHomeRecommendState>(StudioHomeRecommendState.Init)
    val state: StateFlow<StudioHomeRecommendState>
        get() = _state

    private val _event = MutableSharedFlow<StudioHomeRecommendViewEvent>()
    val event: SharedFlow<StudioHomeRecommendViewEvent>
        get() = _event

    private val _list = MutableStateFlow<List<Studio>>(emptyList())
    val list: StateFlow<List<Studio>>
        get() = _list

    val type: String by lazy {
        savedStateHandle.get<String>(StudioHomeRecommendFragment.KEY_TYPE) ?: ""
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            // TODO : Error Handling
        }
    }

    fun getStudioList() {
        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            getStudioListUseCase(limit = 10, offset = 0).onEach { state ->
                when (state) {
                    is ResponseState.Success -> {
                        _list.emit(state.data.studios ?: emptyList())
                        _state.emit(StudioHomeRecommendState.List)
                    }
                    is ResponseState.Error -> {
                        _state.emit(StudioHomeRecommendState.Error)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun clickStudioDetail(studioId: Int?) {
        viewModelScope.launch {
            _event.emit(StudioHomeRecommendViewEvent.ClickDetail(studioId))
        }
    }
}