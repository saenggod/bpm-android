package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.ReviewResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponse
import com.team.bpm.data.network.BPMResponseHandler
import com.team.bpm.data.network.ErrorResponse.Companion.toDataModel
import com.team.bpm.data.network.MainApi
import com.team.bpm.data.util.convertByteArrayToWebpFile
import com.team.bpm.data.util.createImageMultipartBody
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Review
import com.team.bpm.domain.repository.WriteReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class WriteReviewRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : WriteReviewRepository {
    override suspend fun sendReview(
        studioId: Int,
        imageByteArrays: List<ByteArray>,
        rating: Double,
        recommends: List<String>,
        content: String
    ): Flow<ResponseState<Review>> {

        return flow {
            BPMResponseHandler().handle {
                mainApi.sendReview(
                    studioId = 1,
                    files = imageByteArrays.map { imageByteArray ->
                        createImageMultipartBody(
                            key = "file",
                            file = convertByteArrayToWebpFile(imageByteArray)
                        )
                    },
                    rating = rating,
                    recommends = recommends,
                    content = content
                )
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data.toDataModel()))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }
}