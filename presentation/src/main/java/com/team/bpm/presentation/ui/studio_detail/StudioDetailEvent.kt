package com.team.bpm.presentation.ui.studio_detail

import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.Studio

sealed class StudioDetailEvent {
    object LoadStudioDetailInfo : StudioDetailEvent()
    data class OnLoadedStudioDetailInfo(val studio: Studio, val reviews: List<Review>) : StudioDetailEvent()
}