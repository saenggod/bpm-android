package com.team.bpm.presentation.ui.sign_up

import android.os.Bundle
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.usecase.sign_up.SignUpUseCase
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
class SignUpViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val signUpUseCase: SignUpUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), SignUpContract {
    private val _state = MutableStateFlow(SignUpContract.State())
    override val state: StateFlow<SignUpContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SignUpContract.Effect>()
    override val effect: SharedFlow<SignUpContract.Effect> = _effect.asSharedFlow()

    override fun event(event: SignUpContract.Event) = when (event) {
        is SignUpContract.Event.OnClickAddImage -> {
            onClickSetImage()
        }
        is SignUpContract.Event.OnImageAdded -> {
            onImageAdded(event.image)
        }
        is SignUpContract.Event.OnClickSubmit -> {
            onClickSubmit(nickname = event.nickname, bio = event.bio)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getBundle(): Bundle? {
        return savedStateHandle.get<Bundle>(SignUpActivity.KEY_BUNDLE)
    }

    private val kakaoUserInfo: Pair<Long, String> by lazy {
        Pair(
            getBundle()?.getLong(SignUpActivity.KEY_KAKAO_USER_ID) ?: 0L,
            getBundle()?.getString(SignUpActivity.KEY_KAKAO_NICK_NAME) ?: ""
        )
    }

    private fun onImageAdded(image: ImageBitmap) {
        _state.update {
            it.copy(profileImage = image)
        }
    }

    private fun onClickSetImage() {
        viewModelScope.launch {
            _effect.emit(SignUpContract.Effect.AddImage)
        }
    }

    private fun onClickSubmit(
        nickname: String,
        bio: String
    ) {
        viewModelScope.launch {
            if (nickname.isEmpty()) {
                _state.update {
                    it.copy(submittedWithOmission = true)
                }
            } else {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher + exceptionHandler) {
                    signUpUseCase(
                        kakaoId = kakaoUserInfo.first,
                        image = _state.value.profileImage!!.asAndroidBitmap(),
                        nickname = kakaoUserInfo.second,
                        bio = bio
                    ).onEach { result ->
                        when (result) {
                            is ResponseState.Success -> {
                                withContext(mainImmediateDispatcher) {
                                    _effect.emit(SignUpContract.Effect.OnSuccessSignUp)
                                }
                            }
                            is ResponseState.Error -> {
                                if (result.error.code == USER_NICKNAME_ALREADY_EXISTS) {
                                    withContext(mainImmediateDispatcher) {
                                        _effect.emit(SignUpContract.Effect.OnSuccessSignUp)
                                    }
                                } else {
                                    // TODO : Error Handling
                                }
                            }
                        }
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    companion object {
        private const val USER_NICKNAME_ALREADY_EXISTS = "409"
    }
}