package com.team.bpm.presentation.ui.main.studio.recommend

interface StudioHomeRecommendViewEvent {
    data class ClickDetail(val studioId : Int?) : StudioHomeRecommendViewEvent
}