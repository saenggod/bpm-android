package com.team.bpm.presentation.ui.main.mypage.mypost

import com.team.bpm.domain.model.Community
import com.team.bpm.presentation.base.BaseContract

interface MyPostContract :
    BaseContract<MyPostContract.State, MyPostContract.Event, MyPostContract.Effect> {
    data class State(
        val communityList: List<Community>? = null
    )

    sealed interface Event {
        data class OnClickListItem(val communityId: Int) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        data class GoToCommunityDetail(val communityId: Int) : Effect
        object GoOut : Effect
    }
}