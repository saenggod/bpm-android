package com.team.bpm.presentation.ui.register_studio.register_location

import androidx.lifecycle.ViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RegisterLocationViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher

): ViewModel(), RegisterLocationContract {

    private val _state = MutableStateFlow(RegisterLocationContract.State())
    override val state: StateFlow<RegisterLocationContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterLocationContract.Effect>()
    override val effect: SharedFlow<RegisterLocationContract.Effect> = _effect.asSharedFlow()

    override fun event(event: RegisterLocationContract.Event) = when (event) {
        is RegisterLocationContract.Event.OnClickSearch -> {

        }
        is RegisterLocationContract.Event.OnClickChangeLocation -> {

        }
    }


}