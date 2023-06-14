package com.team.bpm.presentation.ui.splash

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.splash.*
import com.team.bpm.domain.usecase.user.SetUserIdUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import com.team.bpm.presentation.ui.sign_up.SignUpContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getKakaoIdUseCase: GetKakaoIdUseCase,
    private val setKakaoIdUseCase: SetKakaoIdUseCase,
    private val getUserTokenUseCase: GetUserTokenUseCase,
    private val setUserTokenUseCase: SetUserTokenUseCase,
    private val setUserIdUseCase: SetUserIdUseCase,
    private val sendKakaoIdVerificationUseCase: SendKakaoIdVerificationUseCase
) : BaseViewModelV2(), SplashContract {

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
            result.id?.let { userId ->
                result.token?.let { userToken ->
                    setUserInfo(userId, userToken)
                }
            }
        }
    }

    private suspend fun setUserInfo(userId: Long, token: String) {
        setUserIdUseCase(userId).zip(setUserTokenUseCase(token)) { userId, userToken ->
            Pair(userId, userToken)
        }.collect { result ->
            withContext(mainImmediateDispatcher) {
                if (result.first != null && result.second != null) {
                    _effect.emit(SplashContract.Effect.GoToMainActivity)
                } else {
                    _effect.emit(SplashContract.Effect.ShowToast("로그인 할 수 없습니다."))
                }
            }
        }
    }
}