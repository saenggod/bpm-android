package com.team.bpm.presentation.ui.main.studio.register.register_location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.register_studio.register_location.GetAddressNameUseCase
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
class RegisterLocationViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getAddressNameUseCase: GetAddressNameUseCase
) : ViewModel(), RegisterLocationContract {

    private val _state = MutableStateFlow(RegisterLocationContract.State())
    override val state: StateFlow<RegisterLocationContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterLocationContract.Effect>()
    override val effect: SharedFlow<RegisterLocationContract.Effect> = _effect.asSharedFlow()

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    override fun event(event: RegisterLocationContract.Event) = when (event) {
        is RegisterLocationContract.Event.OnClickSearch -> {

        }

        is RegisterLocationContract.Event.OnClickChangeLocation -> {
            onClickChangeLocation(event.latitude, event.longitude)
        }
    }

    private fun onClickChangeLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch(ioDispatcher) {
            getAddressNameUseCase(latitude, longitude).onEach { result ->
                withContext(mainImmediateDispatcher) {
                    if (!result.isNullOrEmpty()) {
                        _state.update {
                            it.copy(addressName = result)
                        }
                    } else {
                        _effect.emit(RegisterLocationContract.Effect.ShowToast("주소명을 찾을 수 없습니다."))
                    }
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }
}