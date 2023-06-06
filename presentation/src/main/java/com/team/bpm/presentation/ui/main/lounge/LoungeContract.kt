package com.team.bpm.presentation.ui.main.lounge

import com.team.bpm.presentation.base.BaseContract

interface LoungeContract :
    BaseContract<LoungeContract.State, LoungeContract.Event, LoungeContract.Effect> {
    data class State(
        val currentTabPosition: Int = 0
    )

    sealed interface Event {
        object OnClickAdd : Event
        object OnClickSearch : Event
        object OnClickAddCommunityPost : Event
        object OnClickAddQuestionPost : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object ShowAddBottomSheet : Effect
        object GoToAddCommunityPost : Effect
        object GoToAddQuestionPost : Effect
    }
}