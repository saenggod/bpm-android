package com.team.bpm.presentation.ui.main.bodyshape.detail.posting

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.team.bpm.presentation.base.BaseContract

interface BodyShapeDetailPostingContract : BaseContract<BodyShapeDetailPostingContract.State, BodyShapeDetailPostingContract.Event, BodyShapeDetailPostingContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val imageList: List<Pair<Uri, ImageBitmap>> = emptyList(),
        val isEditing: Boolean = false
    )

    sealed interface Event {
        object GetBodyShapeContent : Event

        data class SetImageListWithLoadedImageList(val loadedImageList: List<Pair<Uri, ImageBitmap>>) :
            Event

        object OnClickImagePlaceHolder : Event

        data class OnImagesAdded(val images: List<Pair<Uri, ImageBitmap>>) : Event

        data class OnClickRemoveImage(val index: Int) : Event

        data class OnClickSubmit(val content: String) : Event
    }

    sealed interface Effect {
        data class OnContentLoaded(val content: String, val images: List<String>) : Effect

        data class ShowToast(val text: String) : Effect

        object AddImages : Effect

        data class RedirectToBodyShape(
            val albumId: Int,
            val bodyShapeId: Int,
            val newIntentNeeded: Boolean,
            val dDay: Int
        ) : Effect
    }
}