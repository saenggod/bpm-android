package com.team.bpm.presentation.ui.register_studio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.usecase.register_studio.RegisterStudioUseCase
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
class RegisterStudioViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val registerStudioUseCase: RegisterStudioUseCase
) : ViewModel(), RegisterStudioContract {

    private val _state = MutableStateFlow(RegisterStudioContract.State())
    override val state: StateFlow<RegisterStudioContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterStudioContract.Effect>()
    override val effect: SharedFlow<RegisterStudioContract.Effect> = _effect.asSharedFlow()

    override fun event(event: RegisterStudioContract.Event) = when (event) {
        is RegisterStudioContract.Event.OnClickSubmit -> {
            with(event) {
                onClickSubmit(
                    name = name,
                    address = address,
                    latitude = latitude,
                    longitude = longitude,
                    phoneNumber = phoneNumber,
                    snsAddress = snsAddress,
                    businessHours = businessHours,
                    priceInfo = priceInfo
                )
            }
        }
        RegisterStudioContract.Event.OnClickSetLocation -> {
            onClickSetLocation()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            // TODO : Error Handling
        }
    }

    private fun onClickSubmit(
        name: String,
        address: String,
        latitude: Double,
        longitude: Double,
        phoneNumber: String,
        snsAddress: String,
        businessHours: String,
        priceInfo: String
    ) {
        viewModelScope.launch { _state.update { it.copy(isLoading = true) } }

        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            registerStudioUseCase(
                name = name,
                address = address,
                latitude = latitude,
                longitude = longitude,
                recommends = state.value.recommendList,
                phone = phoneNumber,
                sns = snsAddress,
                openHours = businessHours,
                price = priceInfo
            ).onEach { result ->
                withContext(mainImmediateDispatcher) {
                    _state.update { it.copy(isLoading = false) }

                    when (result) {
                        is ResponseState.Success -> {
                            // TODO : Show pop up?
                        }
                        is ResponseState.Error -> {
                            _effect.emit(RegisterStudioContract.Effect.ShowToast("스튜디오를 등록할 수 없습니다."))
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun onClickSetLocation() {
        viewModelScope.launch {
            _effect.emit(RegisterStudioContract.Effect.GoToSetLocation)
        }
    }
}