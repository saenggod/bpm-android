package com.team.bpm.presentation.ui.main.mypage

import androidx.lifecycle.viewModelScope
import com.team.bpm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MyPageViewModel @Inject constructor() : BaseViewModel() {

    private val _event = MutableSharedFlow<MyPageViewEvent>()
    val event: SharedFlow<MyPageViewEvent>
        get() = _event

    fun clickDisable() {
        viewModelScope.launch {
            _event.emit(MyPageViewEvent.Click)
        }
    }
}