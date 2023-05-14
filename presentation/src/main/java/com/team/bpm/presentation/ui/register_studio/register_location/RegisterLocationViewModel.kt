package com.team.bpm.presentation.ui.register_studio.register_location

import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    override fun event(event: RegisterLocationContract.Event) = when (event) {
        is RegisterLocationContract.Event.OnClickSearch -> {

        }
        is RegisterLocationContract.Event.OnClickChangeLocation -> {
            onClickChangeLocation(event.latitude, event.longitude, event.geocoder)
        }
    }

    private fun onClickChangeLocation(latitude: Double, longitude: Double, geocoder: Geocoder) {
        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            geocoder.getFromLocation(latitude, longitude, 1)?.first()?.let { address ->
                withContext(mainImmediateDispatcher) {
                    _state.update {
                        it.copy(latitude = latitude, longitude = longitude, addressText = address.getAddressLine(0).drop(5))
                    }
                }
            }
        }
    }
}