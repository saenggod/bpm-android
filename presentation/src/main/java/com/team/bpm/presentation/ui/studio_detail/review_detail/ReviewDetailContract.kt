package com.team.bpm.presentation.ui.studio_detail.review_detail

import com.team.bpm.domain.model.Review
import com.team.bpm.presentation.base.BaseContract

interface ReviewDetailContract : BaseContract<ReviewDetailContract.State, ReviewDetailContract.Event, ReviewDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val review: Review? = null,
        val liked: Boolean? = false
    )

    sealed interface Event {
        object GetReviewDetail : Event
        object OnClickLike : Event
    }

    sealed interface Effect {

    }
}