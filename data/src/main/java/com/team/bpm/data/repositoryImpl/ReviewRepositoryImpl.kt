package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.request.ReportRequest
import com.team.bpm.data.model.response.KeywordListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.ReviewListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.ReviewResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.data.util.convertByteArrayToWebpFile
import com.team.bpm.data.util.createImageMultipartBody
import com.team.bpm.domain.model.KeywordList
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.model.ReviewList
import com.team.bpm.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(private val mainApi: MainApi) : ReviewRepository {

    override suspend fun sendReview(
        studioId: Int,
        imageByteArrays: List<ByteArray>,
        rating: Double,
        recommends: List<Int>,
        content: String
    ): Flow<Review> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendReview(
                    studioId = studioId,
                    files = imageByteArrays.map { imageByteArray ->
                        createImageMultipartBody(
                            key = "files",
                            file = convertByteArrayToWebpFile(imageByteArray)
                        )
                    },
                    rating = rating,
                    recommends = recommends,
                    content = content
                )
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun deleteReview(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteReview(studioId, reviewId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun fetchReviewList(
        studioId: Int
    ): Flow<ReviewList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchReviewList(studioId)
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
                mainApi.fetchReviewDetail(studioId, reviewId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendReviewReport(
        studioId: Int,
        reviewId: Int,
        reason: String
    ): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendReviewReport(studioId, reviewId, ReportRequest(reason))
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun sendReviewLike(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendReviewLike(studioId, reviewId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun deleteReviewLike(
        studioId: Int,
        reviewId: Int
    ): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteReviewLike(studioId, reviewId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun fetchKeywordList(): Flow<KeywordList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchKeywordList()
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }
}