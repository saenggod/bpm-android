package com.team.bpm.presentation.ui.main.home.recommend

interface HomeRecommendViewEvent {
    data class ClickDetail(val studioId : Int?) : HomeRecommendViewEvent
}