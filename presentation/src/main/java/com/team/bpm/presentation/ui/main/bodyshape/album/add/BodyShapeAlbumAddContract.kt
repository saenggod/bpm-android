package com.team.bpm.presentation.ui.main.bodyshape.album.add

import com.team.bpm.presentation.base.BaseContract
import java.time.LocalDate

interface BodyShapeAlbumAddContract : BaseContract<BodyShapeAlbumAddContract.State, BodyShapeAlbumAddContract.Event, BodyShapeAlbumAddContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val albumId: Int? = null,
        val isEditing: Boolean = false,
        val fetchedAlbumName: String? = null,
        val selectedDate: LocalDate? = null,
        val fetchedMemo: String? = null
    )

    sealed interface Event {
        object GetAlbum : Event

        object OnClickEdit : Event

        data class OnClickDate(val date: LocalDate) : Event

        data class OnClickSubmit(
            val albumName: String,
            val memo: String
        ) : Event
    }

    sealed interface Effect {

    }
}