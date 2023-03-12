package com.team.bpm.presentation.ui.studio_detail

import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.Studio

sealed interface StudioDetailState {
    object Init: StudioDetailState
    data class StudioDetailSuccess(val studio: Studio): StudioDetailState
    data class ReviewListSuccess(val reviewList: List<Review>) : StudioDetailState
    object Error: StudioDetailState
}