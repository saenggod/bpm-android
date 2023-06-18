package com.team.bpm.presentation.ui.intro.sign_up

import androidx.compose.ui.graphics.ImageBitmap
import com.team.bpm.presentation.base.BaseContract

interface SignUpContract : BaseContract<SignUpContract.State, SignUpContract.Event, SignUpContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val kakaoNickname: String? = null,
        val profileImage: ImageBitmap? = null,
        val submittedWithOmission: Boolean? = null,
        val errorCode: String? = null
    )

    sealed interface Event {
        object GetKakaoNickname : Event

        object OnClickAddImage : Event

        data class OnImageAdded(val image: ImageBitmap) : Event

        data class OnError(val message: String) : Event

        data class OnClickSubmit(val nickname: String, val bio: String) : Event
    }

    sealed interface Effect {
        object AddImage : Effect

        object OnSuccessSignUp : Effect

        data class ShowToast(val text: String) : Effect
    }
}