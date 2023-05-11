package com.team.bpm.presentation.ui.register_studio.register_location

import com.team.bpm.presentation.base.BaseContract

interface RegisterLocationContract : BaseContract<RegisterLocationContract.State, RegisterLocationContract.Event, RegisterLocationContract.Effect> {
    data class State(
        val isLoading: Boolean = false
    )

    sealed interface Event {
        data class OnClickSearch(val searchKeyword: String) : Event
        data class OnClickChangeLocation(val latitude: Double, val longitude: Double) : Event
    }

    sealed interface Effect {

    }
}