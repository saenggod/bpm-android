package com.team.bpm.presentation.ui.main.mypage.notification

import com.team.bpm.domain.model.Notification
import com.team.bpm.presentation.base.BaseContract

interface MyPageNotificationContract :
    BaseContract<MyPageNotificationContract.State, MyPageNotificationContract.Event, MyPageNotificationContract.Effect> {

    data class State(
        val isLoading: Boolean = false
    )

    sealed interface Event {
        data class ClickNotification(val item: Notification) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        data class GoToNotificationDetail(val item: Notification) : Effect
    }
}