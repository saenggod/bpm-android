package com.team.bpm.presentation.ui.main.add

import androidx.lifecycle.viewModelScope
import com.team.bpm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainAddViewModel @Inject constructor() : BaseViewModel() {

    private val _event = MutableSharedFlow<MainAddViewEvent>()
    val event: SharedFlow<MainAddViewEvent>
        get() = _event

    fun clickDisable() {
        viewModelScope.launch {
            _event.emit(MainAddViewEvent.Click)
        }
    }

}