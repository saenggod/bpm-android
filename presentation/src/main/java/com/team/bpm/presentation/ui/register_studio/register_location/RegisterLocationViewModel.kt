package com.team.bpm.presentation.ui.register_studio.register_location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterLocationViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher

) : ViewModel(), RegisterLocationContract {

    private val _state = MutableStateFlow(RegisterLocationContract.State())
    override val state: StateFlow<RegisterLocationContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterLocationContract.Effect>()
    override val effect: SharedFlow<RegisterLocationContract.Effect> = _effect.asSharedFlow()

    override fun event(event: RegisterLocationContract.Event) = when (event) {
        is RegisterLocationContract.Event.OnClickSearch -> {

        }
        is RegisterLocationContract.Event.OnClickChangeLocation -> {
            onClickChangeLocation(event.latitude, event.longitude)
        }
    }

    private fun onClickChangeLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _state.update {
                it.copy(latitude = latitude, longitude = longitude)
            }
            println("${state.value.latitude} ${state.value.longitude}")
        }
    }
}