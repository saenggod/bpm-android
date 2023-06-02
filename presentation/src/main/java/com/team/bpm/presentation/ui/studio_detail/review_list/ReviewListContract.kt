package com.team.bpm.presentation.ui.studio_detail.review_list

import com.team.bpm.domain.model.Review
import com.team.bpm.presentation.base.BaseContract

interface ReviewListContract : BaseContract<ReviewListContract.State, ReviewListContract.Event, ReviewListContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val originalReviewList: List<Review> = emptyList(),
        val reviewList: List<Review> = emptyList(),
        val isReviewListShowingImageReviewsOnly: Boolean = false,
        val isReviewListSortedByLike: Boolean = true,
    )

    sealed interface Event {
        object GetReviewList : Event

        object OnClickShowImageReviewsOnly : Event

        object OnClickShowNotOnlyImageReviews : Event

        object OnClickSortByLike : Event

        object OnClickSortByDate : Event

        object OnClickWriteReview : Event

        data class OnClickReviewLikeButton(val reviewId: Int) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        data class GoToWriteReview(val studioId: Int) : Effect
    }
}