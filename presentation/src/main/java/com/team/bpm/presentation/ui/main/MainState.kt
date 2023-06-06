package com.team.bpm.presentation.ui.main

sealed interface MainState {
    object Init : MainState
    data class Tab (val startIndex : Int): MainState
}