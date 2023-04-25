package com.team.bpm.presentation.ui.main.studio

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.UserSchedule
import com.team.bpm.domain.usecase.main.GetUserScheduleUseCase
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
class StudioHomeViewModel @Inject constructor(
    private val getUserScheduleUseCase: GetUserScheduleUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _state = MutableStateFlow<StudioHomeState>(StudioHomeState.Init)
    val state: StateFlow<StudioHomeState>
        get() = _state

    private val _event = MutableSharedFlow<StudioHomeViewEvent>()
    val event: SharedFlow<StudioHomeViewEvent>
        get() = _event

    private val _userScheduleInfo = MutableStateFlow(UserSchedule())
    val userScheduleInfo: StateFlow<UserSchedule>
        get() = _userScheduleInfo

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    fun getUserSchedule() {
        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            getUserScheduleUseCase().onEach { state ->
                when (state) {
                    is ResponseState.Success -> {
                        _userScheduleInfo.emit(state.data)
                        _state.emit(StudioHomeState.UserSchedule)
                    }
                    is ResponseState.Error -> {
                        _state.emit(StudioHomeState.Error)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun clickSearch(){
        viewModelScope.launch {
            _event.emit(StudioHomeViewEvent.ClickSearch)
        }
    }

    fun clickSchedule(){
        viewModelScope.launch {
            _event.emit(StudioHomeViewEvent.ClickSchedule)
        }
    }

    fun refreshUserSchedule(){
        viewModelScope.launch {
            _state.emit(StudioHomeState.Init)
        }
    }

}