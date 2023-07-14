package com.team.bpm.presentation.ui.main.lounge.community

import com.team.bpm.domain.model.Community
import com.team.bpm.presentation.base.BaseContract

interface CommunityContract :
    BaseContract<CommunityContract.State, CommunityContract.Event, CommunityContract.Effect> {
    data class State(
        val communityList : List<Community>? = null
    )

    sealed interface Event {
        data class OnClickListItem(val communityId: Int) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object GoToTop : Effect
        data class GoToCommunityDetail(val communityId: Int) : Effect
    }
}