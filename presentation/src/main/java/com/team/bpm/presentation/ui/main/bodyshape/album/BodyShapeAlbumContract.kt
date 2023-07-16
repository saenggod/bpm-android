package com.team.bpm.presentation.ui.main.bodyshape.album

import com.team.bpm.domain.model.BodyShapeSchedule
import com.team.bpm.presentation.base.BaseContract

interface BodyShapeAlbumContract :
    BaseContract<BodyShapeAlbumContract.State, BodyShapeAlbumContract.Event, BodyShapeAlbumContract.Effect> {
    data class State(
        val isLoading: Boolean? = null,
        val albumInfo: BodyShapeSchedule? = null,
        val isPostToday : Boolean? = false
    )

    sealed interface Event {
        object OnClickAddBodyShapeDetailPosting : Event
        object OnClickEditAlbumDetail : Event
        object OnClickMore : Event
        data class OnClickBodyShapeDetail(val albumDetailId: Int) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        data class GoToEditAlbumDetail(val albumId: Int) : Effect
        data class GoToAddBodyShapeDetail(val albumId: Int) : Effect
        data class GoToBodyShapeDetail(val albumId: Int, val albumDetailId: Int, val dday : Int) : Effect
        object ShowMoreBottomSheet : Effect
        object GoOutThisPage : Effect
    }
}