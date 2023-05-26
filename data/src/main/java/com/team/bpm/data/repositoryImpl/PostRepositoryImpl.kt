package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.PostResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.Post
import com.team.bpm.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : PostRepository {
    override suspend fun fetchPostDetail(postId: Int): Flow<Post> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchPostDetail(postId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }
}