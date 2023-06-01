package com.team.bpm.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.splash.*
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class SplashViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getKakaoIdUseCase: GetKakaoIdUseCase,
    private val setKakaoIdUseCase: SetKakaoIdUseCase,
    private val getUserTokenUseCase: GetUserTokenUseCase,
    private val setUserTokenUseCase: SetUserTokenUseCase,
    private val sendKakaoIdVerificationUseCase: SendKakaoIdVerificationUseCase
) : ViewModel(), SplashContract {

    private var kakaoIdForSignUp by Delegates.notNull<Long>()
    private lateinit var kakaoNicknameForSignUp: String

    private val _state = MutableStateFlow(SplashContract.State())
    override val state: StateFlow<SplashContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SplashContract.Effect>()
    override val effect: SharedFlow<SplashContract.Effect> = _effect.asSharedFlow()

    override fun event(event: SplashContract.Event) = when (event) {
        is SplashContract.Event.OnStart -> {
            onStart()
        }

        is SplashContract.Event.GetStoredUserInfo -> {
            getStoredUserInfo()
        }

        is SplashContract.Event.OnClickKakaoButton -> {
            onClickKakaoButton()
        }

        is SplashContract.Event.OnFailureKakaoLogin -> {
            onFailureKakaoLogin()
        }

        is SplashContract.Event.OnSuccessKakaoLogin -> {
            onSuccessKakaoLogin(event.kakaoId, event.kakaoNickname)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            // TODO : GoToSignUp
        }
    }



    private fun onStart() {
        viewModelScope.launch {
            _effect.emit(SplashContract.Effect.Init)
        }
    }

    private fun getStoredUserInfo() {
        viewModelScope.launch(ioDispatcher) {
            getKakaoIdUseCase().zip(getUserTokenUseCase()) { kakaoId, userToken ->
                Pair(kakaoId, userToken)
            }.onEach { result ->
                withContext(mainImmediateDispatcher) {
                    if (result.first != null && result.second != null) {
                        _effect.emit(SplashContract.Effect.GoToMainActivity)
                    } else {
                        _state.update {
                            it.copy(isSignUpNeeded = true)
                        }
                    }
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    private fun onClickKakaoButton() {
        viewModelScope.launch {
            _effect.emit(SplashContract.Effect.GetKakaoUserInfo)
        }
    }

    private fun onFailureKakaoLogin() {
        viewModelScope.launch {
            _effect.emit(SplashContract.Effect.ShowToast("로그인에 실패하였습니다. 다시 시도해 주세요."))
        }
    }

    private fun onSuccessKakaoLogin(
        kakaoId: Long,
        kakaoNickname: String
    ) {
        kakaoIdForSignUp = kakaoId
        kakaoNicknameForSignUp = kakaoNickname

        viewModelScope.launch(ioDispatcher) {
            setKakaoIdUseCase(kakaoId).onEach {
                sendKakaoIdVerification(kakaoId)
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    private suspend fun sendKakaoIdVerification(kakaoId: Long) {
        sendKakaoIdVerificationUseCase(kakaoId).collect { result ->
            result.token?.let { userToken ->
                saveUserToken(userToken)
            } ?: run {
                _effect.emit(SplashContract.Effect.GoToSignUpActivity(kakaoIdForSignUp, kakaoNicknameForSignUp))
            }
        }
    }

    private suspend fun saveUserToken(userToken: String) {
        setUserTokenUseCase(userToken).collect {
            withContext(mainImmediateDispatcher) {
                _effect.emit(SplashContract.Effect.GoToMainActivity)
            }
        }
    }
}