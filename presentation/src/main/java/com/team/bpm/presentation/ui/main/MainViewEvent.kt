package com.team.bpm.presentation.ui.main

sealed interface MainViewEvent {
    object Add : MainViewEvent
    data class MoveTab(val tabIndex : Int) : MainViewEvent
}