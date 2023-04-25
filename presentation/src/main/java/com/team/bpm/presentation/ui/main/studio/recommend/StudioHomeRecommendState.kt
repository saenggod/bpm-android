package com.team.bpm.presentation.ui.main.studio.recommend

sealed interface StudioHomeRecommendState {
    object Init : StudioHomeRecommendState
    object List : StudioHomeRecommendState

    object Error : StudioHomeRecommendState
}