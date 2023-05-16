package com.team.bpm.presentation.ui.main.community.community_detail

import com.team.bpm.domain.model.Post
import com.team.bpm.presentation.base.BaseContract

interface CommunityDetailContract : BaseContract<CommunityDetailContract.State, CommunityDetailContract.Event, CommunityDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val post: Post? = null
    )

    sealed interface Event {
        object GetCommunityDetail : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
    }
}