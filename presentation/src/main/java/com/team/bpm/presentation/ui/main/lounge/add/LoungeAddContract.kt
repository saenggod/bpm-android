package com.team.bpm.presentation.ui.main.lounge.add

import com.team.bpm.presentation.base.BaseContract

interface LoungeAddContract :
    BaseContract<LoungeAddContract.State, LoungeAddContract.Event, LoungeAddContract.Effect> {
    data class State(
        val currentTabPosition: Int = 0
    )

    sealed interface Event {
        object OnClickAddCommunityPost : Event
        object OnClickAddQuestionPost : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object GoToAddCommunityPost : Effect
        object GoToAddQuestionPost : Effect
    }
}