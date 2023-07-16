package com.team.bpm.presentation.ui.main.bodyshape.album.more

import com.team.bpm.presentation.base.BaseContract

interface BodyShapeAlbumMoreContract :
    BaseContract<BodyShapeAlbumMoreContract.State, BodyShapeAlbumMoreContract.Event, BodyShapeAlbumMoreContract.Effect> {
    data class State(
        val isLoading: Boolean? = null
    )

    sealed interface Event {
        object Edit : Event
        object Delete : Event
    }

    sealed interface Effect {
        object GoEdit : Effect
        object GoDelete : Effect
    }
}