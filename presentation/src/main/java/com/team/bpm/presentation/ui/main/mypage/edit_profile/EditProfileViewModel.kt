package com.team.bpm.presentation.ui.main.mypage.edit_profile

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.user.GetUserProfileUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : BaseViewModelV2(), EditProfileContract {
    private val _state = MutableStateFlow(EditProfileContract.State())
    override val state: StateFlow<EditProfileContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<EditProfileContract.Effect>()
    override val effect: SharedFlow<EditProfileContract.Effect> = _effect.asSharedFlow()

    override fun event(event: EditProfileContract.Event) = when (event) {
        is EditProfileContract.Event.GetProfile -> {
            getProfile()
        }

        is EditProfileContract.Event.OnClickAddImage -> {
            onClickAddImage()
        }

        is EditProfileContract.Event.OnImageAdded -> {
            onImageAdded(event.image)
        }

        is EditProfileContract.Event.OnError -> {
            onError(event.message)
        }

        is EditProfileContract.Event.OnClickSubmit -> {
            onClickSubmit(event.nickname, event.bio)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            withContext(ioDispatcher) {
                getUserProfileUseCase().onEach { result ->
                    withContext(mainImmediateDispatcher) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                userProfile = result
                            )
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
            }
        }
    }

    private fun onClickAddImage() {
        viewModelScope.launch {
            _effect.emit(EditProfileContract.Effect.AddImage)
        }
    }

    private fun onImageAdded(image: ImageBitmap) {
        viewModelScope.launch {
            _state.update {
                it.copy(image = image)
            }
        }
    }

    private fun onError(message: String) {
        viewModelScope.launch {
            _effect.emit(EditProfileContract.Effect.ShowToast(message))
        }
    }

    private fun onClickSubmit(nickname: String, bio: String) {

    }
}