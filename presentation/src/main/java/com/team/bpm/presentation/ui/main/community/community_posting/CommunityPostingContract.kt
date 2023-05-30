package com.team.bpm.presentation.ui.main.community.community_posting

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.team.bpm.presentation.base.BaseContract

interface CommunityPostingContract : BaseContract<CommunityPostingContract.State, CommunityPostingContract.Event, CommunityPostingContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val imageList: List<Pair<Uri, ImageBitmap>> = emptyList()
    )

    sealed interface Event {
        object OnClickImagePlaceHolder : Event
        data class OnImagesAdded(val images: List<Pair<Uri, ImageBitmap>>) : Event
        data class OnClickRemoveImage(val index: Int) : Event
        data class OnClickSubmit(val content: String) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object AddImages : Effect
        data class RedirectToCommunity(val communityId: Int) : Effect
    }
}