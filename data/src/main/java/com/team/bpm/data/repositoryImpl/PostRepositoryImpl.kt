package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.PostResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponse
import com.team.bpm.data.network.BPMResponseHandler
import com.team.bpm.data.network.ErrorResponse.Companion.toDataModel
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.Post
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : PostRepository {
    override suspend fun fetchPostDetail(postId: Int): Flow<ResponseState<Post>> {
        return flow {
            BPMResponseHandler().handle {
                mainApi.fetchPostDetail(postId)
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data.toDataModel()))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }
}