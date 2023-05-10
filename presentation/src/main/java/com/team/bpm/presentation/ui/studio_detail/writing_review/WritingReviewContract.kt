package com.team.bpm.presentation.ui.studio_detail.writing_review

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.base.BaseContract

interface WritingReviewContract : BaseContract<WritingReviewContract.State, WritingReviewContract.Event, WritingReviewContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val studio: Studio? = null,
        val imageList: List<Pair<Uri, ImageBitmap>> = emptyList()
    )

    sealed interface Event {
        object GetStudio : Event
        object OnClickImagePlaceHolder : Event
        data class OnImagesAdded(val images: List<Pair<Uri, ImageBitmap>>) : Event
        data class OnClickRemoveImage(val index: Int) : Event
        object OnClickSubmit : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object AddImages : Effect
        object RemoveImage : Effect
    }
}