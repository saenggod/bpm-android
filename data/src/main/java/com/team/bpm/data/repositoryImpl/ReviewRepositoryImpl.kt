package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.ReviewListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.ReviewResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.ReviewList
import com.team.bpm.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : ReviewRepository {
    override suspend fun fetchReviewList(
        studioId: Int
    ): Flow<ReviewList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchReviewList(studioId = studioId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun fetchReviewDetail(
        studioId: Int,
        reviewId: Int
    ): Flow<Review> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchReviewDetail(
                    studioId = studioId,
                    reviewId = reviewId
                )
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun likeReview(
        studioId: Int,
        reviewId: Int
    ): Flow<ResponseBody> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendReviewLike(
                    studioId = studioId,
                    reviewId = reviewId
                )
            }.onEach { result ->
                result.response?.let { emit(it) }
            }.collect()
        }
    }

    override suspend fun dislikeReview(
        studioId: Int,
        reviewId: Int)
    : Flow<ResponseBody> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteReviewLike(
                    studioId = studioId,
                    reviewId = reviewId
                )
            }.onEach { result ->
                result.response?.let { emit(it) }
            }.collect()
        }
    }
}