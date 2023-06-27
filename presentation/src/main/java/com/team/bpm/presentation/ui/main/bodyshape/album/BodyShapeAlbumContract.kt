package com.team.bpm.presentation.ui.main.bodyshape.album

import com.team.bpm.presentation.base.BaseContract

interface BodyShapeAlbumContract :
    BaseContract<BodyShapeAlbumContract.State, BodyShapeAlbumContract.Event, BodyShapeAlbumContract.Effect> {
    data class State(
        val isLoading: Boolean? = null,
    )

    sealed interface Event {

    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
    }
}