package com.team.bpm.presentation.ui.intro.splash

import com.team.bpm.presentation.base.BaseContract

interface SplashContract : BaseContract<SplashContract.State, SplashContract.Event, SplashContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val isSignUpNeeded: Boolean = false,
    )

    sealed interface Event {
        object OnStart : Event

        object OnClickKakaoButton : Event

        object GetStoredUserInfo : Event

        object OnFailureKakaoLogin : Event

        data class OnSuccessKakaoLogin(
            val kakaoId: Long,
            val kakaoNickname: String
        ) : Event


    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object Init : Effect

        object GetKakaoUserInfo : Effect

        object GoToMainActivity : Effect

        data class GoToSignUpActivity(val kakaoId: Long, val kakaoNickname: String) : Effect
    }
}