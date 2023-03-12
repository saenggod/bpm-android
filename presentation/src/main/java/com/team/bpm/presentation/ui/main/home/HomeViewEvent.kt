package com.team.bpm.presentation.ui.main.home

sealed interface HomeViewEvent {
    object ClickSearch : HomeViewEvent
    object ClickSchedule : HomeViewEvent
}