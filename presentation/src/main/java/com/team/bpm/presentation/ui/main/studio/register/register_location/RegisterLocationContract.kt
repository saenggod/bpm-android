package com.team.bpm.presentation.ui.main.studio.register.register_location

import com.team.bpm.presentation.base.BaseContract

interface RegisterLocationContract : BaseContract<RegisterLocationContract.State, RegisterLocationContract.Event, RegisterLocationContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val addressName: String = ""
    )

    sealed interface Event {
        data class OnClickSearch(val searchKeyword: String) : Event
        data class OnClickChangeLocation(val latitude: Double, val longitude: Double) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
    }
}