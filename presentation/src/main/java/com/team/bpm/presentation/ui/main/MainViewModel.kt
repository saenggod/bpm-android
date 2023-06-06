package com.team.bpm.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.mypage.GetMainTabIndexUseCase
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainDispatcher
import com.team.bpm.presentation.model.MainTabType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMainTabIndexUseCase: GetMainTabIndexUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _event = MutableSharedFlow<MainViewEvent>()
    val event: SharedFlow<MainViewEvent>
        get() = _event

    private val _state = MutableStateFlow<MainState>(MainState.Init)
    val state: StateFlow<MainState>
        get() = _state

    private val _startTabIndex = MutableSharedFlow<Int>()
    val startTabIndex: SharedFlow<Int>
        get() = _startTabIndex

    fun onClickAdd() {
        viewModelScope.launch(mainDispatcher) {
            _event.emit(MainViewEvent.Add)
        }
    }

    fun getMainTabIndex() {
        viewModelScope.launch(ioDispatcher) {
            getMainTabIndexUseCase().onEach { index ->
                _startTabIndex.emit(index ?: 0)
                _state.emit(MainState.Tab(index ?: 0))
            }.launchIn(viewModelScope)
        }
    }

}