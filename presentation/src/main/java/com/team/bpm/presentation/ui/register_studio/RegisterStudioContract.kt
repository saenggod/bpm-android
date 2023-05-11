package com.team.bpm.presentation.ui.register_studio

import com.team.bpm.presentation.base.BaseContract

interface RegisterStudioContract : BaseContract<RegisterStudioContract.State, RegisterStudioContract.Event, RegisterStudioContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val addressText: String = "",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val recommendKeywordMap: HashMap<String, Boolean> = HashMap(),
        val recommendKeywordCount: Int = 0
    )

    sealed interface Event {
        data class OnClickSubmit(
            val name: String,
            val address: String,
            val latitude: Double,
            val longitude: Double,
            val phoneNumber: String,
            val snsAddress: String,
            val businessHours: String,
            val priceInfo: String
        ) : Event

        object OnClickSetLocation : Event
        data class OnClickKeywordChip(val keyword: String) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object GoToSetLocation : Effect
    }
}