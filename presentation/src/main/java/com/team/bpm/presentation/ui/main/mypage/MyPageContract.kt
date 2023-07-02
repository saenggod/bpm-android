package com.team.bpm.presentation.ui.main.mypage

import com.team.bpm.presentation.base.BaseContract

interface MyPageContract :
    BaseContract<MyPageContract.State, MyPageContract.Event, MyPageContract.Effect> {
    data class State(
        val userName: String? = null,
        val userDescription: String? = null,
        val userImage: String? = null,
        val isNewNotification: Boolean? = null,
    )

    sealed interface Event {
        object Profile : Event
        object MyPost : Event
        object MyQuestion : Event
        object ScrappedStudios : Event
        object StartTab : Event
        object Notification : Event
        object Notice : Event
        object Version : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object GoNotification : Effect
        object GoMyPost : Effect
        object GoMyQuestion : Effect
        object GoScrappedStudios : Effect
        object GoProfileManage : Effect
        object GoEditStartTab : Effect
    }
}