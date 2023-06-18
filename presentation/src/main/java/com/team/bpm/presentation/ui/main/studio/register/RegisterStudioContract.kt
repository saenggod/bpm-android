package com.team.bpm.presentation.ui.main.studio.register

import com.team.bpm.domain.model.request_wrapper.RegisterStudioWrapper
import com.team.bpm.presentation.base.BaseContract

interface RegisterStudioContract : BaseContract<RegisterStudioContract.State, RegisterStudioContract.Event, RegisterStudioContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val addressName: String = "",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val recommendKeywordMap: HashMap<String, Boolean> = HashMap(),
        val recommendKeywordCount: Int = 0
    )

    sealed interface Event {
        data class OnClickSubmit(val registerStudioWrapper: RegisterStudioWrapper) : Event
        object OnClickSetLocation : Event
        data class OnClickKeywordChip(val keyword: String) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object GoToSetLocation : Effect
    }
}