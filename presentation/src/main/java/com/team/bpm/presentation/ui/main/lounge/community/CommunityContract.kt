package com.team.bpm.presentation.ui.main.lounge.community

import com.team.bpm.presentation.base.BaseContract

interface CommunityContract :
    BaseContract<CommunityContract.State, CommunityContract.Event, CommunityContract.Effect> {
    data class State(
        val isInitialize: Boolean = false
    )

    sealed interface Event {
        object OnClickAdd : Event
        object OnClickChangeTab : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object ShowAddBottomSheet : Effect
    }
}