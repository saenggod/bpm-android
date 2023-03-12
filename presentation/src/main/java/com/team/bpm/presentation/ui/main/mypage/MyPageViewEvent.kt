package com.team.bpm.presentation.ui.main.mypage

sealed interface MyPageViewEvent {
    object Click : MyPageViewEvent
}