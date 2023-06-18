package com.team.bpm.presentation.ui.main.studio.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.request_wrapper.RegisterStudioWrapper
import com.team.bpm.domain.usecase.register_studio.RegisterStudioUseCase
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
            onClickSubmit(event.registerStudioWrapper)
        }

        is RegisterStudioContract.Event.OnClickSetLocation -> {
            onClickSetLocation()
        }

        is RegisterStudioContract.Event.OnClickKeywordChip -> {
            onClickKeywordChip(event.keyword)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun onClickSubmit(registerStudioWrapper: RegisterStudioWrapper) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
        }

        viewModelScope.launch(ioDispatcher) {
            registerStudioUseCase(registerStudioWrapper).onEach {
                withContext(mainImmediateDispatcher) {
                    _state.update { it.copy(isLoading = false) }
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    private fun onClickSetLocation() {
        viewModelScope.launch {
            _effect.emit(RegisterStudioContract.Effect.GoToSetLocation)
        }
    }

    private fun onClickKeywordChip(keyword: String) {
        viewModelScope.launch {
            with(state.value) {
                if (recommendKeywordMap[keyword] == true) {
                    _state.update {
                        it.copy(
                            recommendKeywordMap = it.recommendKeywordMap.toMutableMap().apply {
                                this[keyword] = false
                            } as HashMap<String, Boolean>,
                            recommendKeywordCount = recommendKeywordCount - 1
                        )
                    }
                } else {
                    if (recommendKeywordCount == 5) {
                        _effect.emit(RegisterStudioContract.Effect.ShowToast("5개 이상 선택할 수 없습니다."))
                    } else {
                        _state.update {
                            it.copy(
                                recommendKeywordMap = it.recommendKeywordMap.toMutableMap().apply {
                                    this[keyword] = true
                                } as HashMap<String, Boolean>,
                                recommendKeywordCount = recommendKeywordCount + 1
                            )
                        }
                    }
                }
            }
        }
    }
}