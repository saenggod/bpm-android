package com.team.bpm.presentation.ui.main.search

import com.team.bpm.presentation.base.BaseContract

interface SearchContract : BaseContract<SearchContract.State, SearchContract.Event, SearchContract.Effect> {
    data class State(
        val isLoading: Boolean = false
    )

    sealed interface Event {
        object OnClickSearch : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
    }
}