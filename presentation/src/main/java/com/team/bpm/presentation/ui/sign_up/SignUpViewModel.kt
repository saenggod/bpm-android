package com.team.bpm.presentation.ui.sign_up

import android.os.Bundle
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.sign_up.SignUpUseCase
import com.team.bpm.domain.usecase.splash.SetUserTokenUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import com.team.bpm.presentation.util.convertImageBitmapToByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val signUpUseCase: SignUpUseCase,
    private val setUserTokenUseCase: SetUserTokenUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), SignUpContract {
    private val _state = MutableStateFlow(SignUpContract.State())
    override val state: StateFlow<SignUpContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SignUpContract.Effect>()
    override val effect: SharedFlow<SignUpContract.Effect> = _effect.asSharedFlow()

    override fun event(event: SignUpContract.Event) = when (event) {
        is SignUpContract.Event.GetKakaoNickname -> {
            setKakaoNickname()
        }

        is SignUpContract.Event.OnClickAddImage -> {
            onClickAddImage()
        }

        is SignUpContract.Event.OnImageAdded -> {
            onImageAdded(event.image)
        }

        is SignUpContract.Event.OnError -> {
            onError(event.message)
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

    private val kakaoUserInfo: Pair<Long?, String?> by lazy {
        Pair(
            getBundle()?.getLong(SignUpActivity.KEY_KAKAO_USER_ID),
            getBundle()?.getString(SignUpActivity.KEY_KAKAO_NICK_NAME)
        )
    }

    private fun setKakaoNickname() {
        viewModelScope.launch {
            _state.update {
                it.copy(kakaoNickname = kakaoUserInfo.second)
            }
        }
    }

    private fun onImageAdded(image: ImageBitmap) {
        viewModelScope.launch {
            _state.update {
                it.copy(profileImage = image)
            }
        }
    }

    private fun onClickAddImage() {
        viewModelScope.launch {
            _effect.emit(SignUpContract.Effect.AddImage)
        }
    }

    private fun onError(message: String) {
        viewModelScope.launch {
            _effect.emit(SignUpContract.Effect.ShowToast(message))
        }
    }

    private fun onClickSubmit(
        nickname: String,
        bio: String
    ) {
        kakaoUserInfo.first?.let { kakaoId ->
            viewModelScope.launch {
                if (nickname.isEmpty()) {
                    _state.update {
                        it.copy(submittedWithOmission = true)
                    }
                } else {
                    _state.update {
                        it.copy(isLoading = true)
                    }

                    withContext(ioDispatcher) {
                        state.value.profileImage?.let { profileImage ->
                            signUpUseCase(
                                kakaoId = kakaoId,
                                imageByteArray = convertImageBitmapToByteArray(profileImage),
                                nickname = nickname,
                                bio = bio
                            ).onEach { result ->
                                withContext(mainImmediateDispatcher) {
                                    result.token?.let { token ->
                                        saveUserToken(token)
                                    }
                                }
                            }.launchIn(viewModelScope + exceptionHandler)
                        }
                    }
                }
            }
        } ?: run {
            // TODO : Error Handling
        }
    }

    private fun saveUserToken(token: String) {
        viewModelScope.launch(ioDispatcher) {
            setUserTokenUseCase(token).onEach { tokenResult ->
                withContext(mainImmediateDispatcher) {
                    if (!tokenResult.isNullOrEmpty()) {
                        _effect.emit(SignUpContract.Effect.OnSuccessSignUp)
                    } else {
                        _effect.emit(SignUpContract.Effect.ShowToast("로그인에 실패하였습니다."))
                        _state.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }
}