package com.team.bpm.presentation.ui.main.mypage

import com.team.bpm.presentation.base.BaseContract

interface MyPageContract :
    BaseContract<MyPageContract.State, MyPageContract.Event, MyPageContract.Effect> {
    data class State(
        val userName: String? = null,
        val userDescription: String? = null,
        val alarmCnt: Int? = null,
        val postCnt: Int? = null,
        val scrapCnt: Int? = null
    )

    sealed interface Event {
        object UserData : Event
        object Profile : Event
        object PostHistory : Event
        object StartTab : Event
        object Notification : Event
        object Notice : Event
        object Version : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object GoProfileManage : Effect
        object GoHistoryPost : Effect
        object GoEditStartTab : Effect
//        object GoNotification : Effect // 토스트로 대체
//        object GoNotice : Effect // 토스트로 대체
//        object GoVersionInfo : Effect // 토스트로 대체
    }
}