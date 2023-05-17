package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.ReviewListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.ReviewResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponse
import com.team.bpm.data.network.BPMResponseHandler
import com.team.bpm.data.network.ErrorResponse.Companion.toDataModel
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.ResponseState
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
    ): Flow<ResponseState<ReviewList>> {
        return flow {
            BPMResponseHandler().handle {
                mainApi.fetchReviewList(studioId = studioId)
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data.toDataModel()))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }

    override suspend fun fetchReviewDetail(
        studioId: Int,
        reviewId: Int
    ): Flow<ResponseState<Review>> {
        return flow {
            BPMResponseHandler().handle {
                mainApi.fetchReviewDetail(
                    studioId = studioId,
                    reviewId = reviewId
                )
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data.toDataModel()))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }

    override suspend fun likeReview(
        studioId: Int,
        reviewId: Int
    ): Flow<ResponseState<ResponseBody>> {
        return flow {
            BPMResponseHandler().handle {
                mainApi.sendReviewLike(
                    studioId = studioId,
                    reviewId = reviewId
                )
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }

    override suspend fun dislikeReview(
        studioId: Int,
        reviewId: Int)
    : Flow<ResponseState<ResponseBody>> {
        return flow {
            BPMResponseHandler().handle {
                mainApi.deleteReviewLike(
                    studioId = studioId,
                    reviewId = reviewId
                )
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }
}