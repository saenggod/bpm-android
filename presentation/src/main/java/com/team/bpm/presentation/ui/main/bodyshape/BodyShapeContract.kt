package com.team.bpm.presentation.ui.main.bodyshape

import com.team.bpm.domain.model.BodyShapeSchedules
import com.team.bpm.presentation.base.BaseContract

interface BodyShapeContract :
    BaseContract<BodyShapeContract.State, BodyShapeContract.Event, BodyShapeContract.Effect> {
    data class State(
        val bodyShapeInfo: BodyShapeSchedules? = null,
        val isEmpty: Boolean = false
    )

    sealed interface Event {
        data class OnClickAlbumDetail(val id: Int) : Event
        data class OnClickBodyShapePosting(val id: Int) : Event
        data class OnClickBodyShapeDetail(val albumId: Int, val bodyShapeDetailId : Int, val dday : Int) : Event
        object OnClickCreateBodyShape : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        data class GoBodyAlbumDetail(val id: Int) : Effect
        data class GoBodyShapePosting(val id: Int) : Effect
        data class GoBodyShapeDetail(val albumId: Int, val bodyShapeDetailId : Int, val dday : Int) : Effect
        object GoCreateBodyShape : Effect
    }
}