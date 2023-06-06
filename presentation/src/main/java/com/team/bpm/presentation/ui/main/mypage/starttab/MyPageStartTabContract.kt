package com.team.bpm.presentation.ui.main.mypage.starttab

import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.MainTabType

interface MyPageStartTabContract :
    BaseContract<MyPageStartTabContract.State, MyPageStartTabContract.Event, MyPageStartTabContract.Effect> {

    data class State(
        val startTabIndex: Int? = null,
        val tabList: List<MainTabType>? = null
    )

    sealed interface Event {
        data class EditTab(val index: Int) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
    }
}