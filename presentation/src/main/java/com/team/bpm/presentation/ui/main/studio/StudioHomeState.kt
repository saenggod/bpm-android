package com.team.bpm.presentation.ui.main.studio

sealed interface StudioHomeState {
    object Init : StudioHomeState
    object Album : StudioHomeState

    object Error : StudioHomeState
}