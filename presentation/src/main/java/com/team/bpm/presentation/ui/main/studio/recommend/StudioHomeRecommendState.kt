package com.team.bpm.presentation.ui.main.studio.recommend

import com.team.bpm.domain.model.Error

sealed interface StudioHomeRecommendState {
    object Init : StudioHomeRecommendState
    object List : StudioHomeRecommendState

    data class Error(val error : com.team.bpm.domain.model.Error) : StudioHomeRecommendState
}