package com.team.bpm.presentation.ui.main.mypage.edit_profile

import androidx.compose.ui.graphics.ImageBitmap
import com.team.bpm.domain.model.UserProfile
import com.team.bpm.presentation.base.BaseContract

interface EditProfileContract : BaseContract<EditProfileContract.State, EditProfileContract.Event, EditProfileContract.Effect> {

    data class State(
        val isLoading: Boolean = false,
        val userProfile: UserProfile? = null,
        val image: ImageBitmap? = null,
        val errorCode: String = ""
    )

    sealed interface Event {
        object GetProfile : Event

        object OnClickAddImage : Event

        data class OnImageAdded(val image: ImageBitmap) : Event

        data class OnError(val message: String) : Event

        data class OnClickSubmit(val nickname: String, val bio: String) : Event
    }

    sealed interface Effect {
        object AddImage : Effect

        data class ShowToast(val text: String) : Effect

        object EditSuccess : Effect
    }
}