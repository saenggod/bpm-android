package com.team.bpm.presentation.ui.main.community.community_posting

import androidx.compose.ui.graphics.ImageBitmap
import com.team.bpm.presentation.base.UnidirectionalViewModel

interface CommunityPostingContract : UnidirectionalViewModel<CommunityPostingContract.State, CommunityPostingContract.Event, CommunityPostingContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val imageList: List<ImageBitmap> = emptyList()
    )

    sealed class Event {
        object OnClickBackButton : Event()
        object OnClickImagePlaceHolder : Event()
        data class OnImagesAdded(val images: List<ImageBitmap>) : Event()
        object OnClickRemoveImage : Event()
    }

    sealed class Effect {
        object GoBack : Effect()
        object AddImages : Effect()
        object RemoveImage : Effect()
    }
}