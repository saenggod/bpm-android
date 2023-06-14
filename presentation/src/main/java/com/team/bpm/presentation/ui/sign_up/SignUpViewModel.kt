package com.team.bpm.presentation.ui.sign_up

import android.os.Bundle
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.sign_up.SignUpUseCase
import com.team.bpm.domain.usecase.splash.SetUserTokenUseCase
import com.team.bpm.domain.usecase.user.SetUserIdUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import com.team.bpm.presentation.util.convertImageBitmapToByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val setUserTokenUseCase: SetUserTokenUseCase,
    private val setUserIdUseCase: SetUserIdUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModelV2(), SignUpContract {
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
                                result.id?.let { userId ->
                                    result.token?.let { userToken ->
                                        setUserInfo(userId, userToken)
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

    private suspend fun setUserInfo(userId: Long, token: String) {
        setUserIdUseCase(userId).zip(setUserTokenUseCase(token)) { userId, userToken ->
            Pair(userId, userToken)
        }.collect { result ->
            withContext(mainImmediateDispatcher) {
                if (result.first != null && result.second != null) {
                    _effect.emit(SignUpContract.Effect.OnSuccessSignUp)
                } else {
                    _effect.emit(SignUpContract.Effect.ShowToast("로그인에 실패하였습니다."))
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }
}