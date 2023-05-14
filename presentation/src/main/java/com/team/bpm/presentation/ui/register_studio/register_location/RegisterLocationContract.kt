package com.team.bpm.presentation.ui.register_studio.register_location

import android.location.Geocoder
import com.team.bpm.presentation.base.BaseContract

interface RegisterLocationContract : BaseContract<RegisterLocationContract.State, RegisterLocationContract.Event, RegisterLocationContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val addressText: String = ""
    )

    sealed interface Event {
        data class OnClickSearch(val searchKeyword: String) : Event
        data class OnClickChangeLocation(val latitude: Double, val longitude: Double, val geocoder: Geocoder) : Event
    }

    sealed interface Effect {

    }
}