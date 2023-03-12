package com.team.bpm.presentation.ui.schedule

sealed interface ScheduleViewEvent {

    object Save : ScheduleViewEvent
}