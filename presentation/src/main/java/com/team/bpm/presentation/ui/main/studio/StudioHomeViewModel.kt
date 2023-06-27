package com.team.bpm.presentation.ui.main.studio

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Album
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.usecase.main.GetAlbumUseCase
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class StudioHomeViewModel @Inject constructor(
    private val getAlbumUseCase: GetAlbumUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _state = MutableStateFlow<StudioHomeState>(StudioHomeState.Init)
    val state: StateFlow<StudioHomeState>
        get() = _state

    private val _event = MutableSharedFlow<StudioHomeViewEvent>()
    val event: SharedFlow<StudioHomeViewEvent>
        get() = _event

    private val _albumInfo = MutableStateFlow(Album())
    val albumInfo: StateFlow<Album>
        get() = _albumInfo

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    fun getAlbum() {
        viewModelScope.launch(ioDispatcher) {
            getAlbumUseCase().onEach { state ->
                when (state) {
                    is ResponseState.Success -> {
                        _albumInfo.emit(state.data)
                        _state.emit(StudioHomeState.Album)
                    }

                    is ResponseState.Error -> {
                        _state.emit(StudioHomeState.Error)
                    }
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    fun clickSearch() {
        viewModelScope.launch {
            _event.emit(StudioHomeViewEvent.ClickSearch)
        }
    }

    fun clickAlbum() {
        viewModelScope.launch {
            _event.emit(StudioHomeViewEvent.ClickAlbum)
        }
    }

    fun refreshAlbum() {
        viewModelScope.launch {
            _state.emit(StudioHomeState.Init)
        }
    }
}