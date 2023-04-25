package com.team.bpm.presentation.ui.main.studio

sealed interface StudioHomeState {
    object Init : StudioHomeState
    object UserSchedule : StudioHomeState

    object Error : StudioHomeState
}