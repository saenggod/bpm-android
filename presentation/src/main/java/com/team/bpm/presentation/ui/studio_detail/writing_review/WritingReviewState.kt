package com.team.bpm.presentation.ui.studio_detail.writing_review

import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.Studio

sealed interface WritingReviewState {
    object Init : WritingReviewState
    object Loading : WritingReviewState
    data class StudioSuccess(val studio: Studio) : WritingReviewState
    data class ReviewSuccess(val review: Review) : WritingReviewState
    object Error : WritingReviewState
}