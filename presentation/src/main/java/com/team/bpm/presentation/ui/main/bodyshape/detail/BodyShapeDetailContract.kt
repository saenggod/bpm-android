package com.team.bpm.presentation.ui.main.bodyshape.detail

import com.team.bpm.domain.model.BodyShape
import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.BottomSheetButton

interface BodyShapeDetailContract : BaseContract<BodyShapeDetailContract.State, BodyShapeDetailContract.Event, BodyShapeDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val bodyShape: BodyShape? = null,
        val isBottomSheetShowing: Boolean = false,
        val bottomSheetButtonList: List<BottomSheetButton> = emptyList(),
        val isNoticeDialogShowing: Boolean = false,
        val noticeDialogContent: String = "",
        val isNoticeToQuitDialogShowing: Boolean = false,
        val noticeToQuitDialogContent: String = ""
    )

    sealed interface Event {
        object OnClickBodyShapeActionButton : Event

        object OnClickEditBodyShape : Event

        object OnClickDeleteBodyShape : Event

        object GetBodyShapeDetail : Event

        object OnClickDismissNoticeDialog : Event

        object OnClickDismissNoticeToQuitDialog : Event

        object OnBottomSheetHide : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object GoBack : Effect

        data class GoToEdit(
            val albumId: Int,
            val bodyShapeId: Int
        ) : Effect
    }
}