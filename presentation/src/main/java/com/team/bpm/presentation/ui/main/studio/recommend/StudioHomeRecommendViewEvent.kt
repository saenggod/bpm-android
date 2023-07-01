package com.team.bpm.presentation.ui.main.studio.recommend

interface StudioHomeRecommendViewEvent {
    data class ClickDetail(val studioId : Int?) : StudioHomeRecommendViewEvent
    data class ClickScrap(val studioId : Int?) : StudioHomeRecommendViewEvent
    data class ClickScrapCancel(val studioId : Int?) : StudioHomeRecommendViewEvent
}