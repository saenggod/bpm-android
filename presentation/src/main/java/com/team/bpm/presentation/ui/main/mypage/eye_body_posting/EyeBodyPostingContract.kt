package com.team.bpm.presentation.ui.main.mypage.eye_body_posting

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.team.bpm.presentation.base.BaseContract

interface EyeBodyPostingContract : BaseContract<EyeBodyPostingContract.State, EyeBodyPostingContract.Event, EyeBodyPostingContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val imageList: List<Pair<Uri, ImageBitmap>> = emptyList()
    )

    sealed interface Event {
        object OnClickBackButton : Event
        object OnClickImagePlaceHolder : Event
        data class OnImagesAdded(val images: List<Pair<Uri, ImageBitmap>>) : Event
        data class OnClickRemoveImage(val index: Int) : Event
    }

    sealed interface Effect {
        object GoBack : Effect
        object AddImages : Effect
    }
}