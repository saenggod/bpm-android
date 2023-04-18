package com.team.bpm.presentation.ui.studio_detail

import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.Studio

data class StudioDetailState(
    val isLoading: Boolean = false,
    val studio: Studio? = null,
    val reviews: List<Review>? = null,
    val isLiked: Boolean = false
)
