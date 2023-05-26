package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.request.CommentRequest
import com.team.bpm.data.model.response.CommentListResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.CommentList
import com.team.bpm.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : CommentRepository {
    override suspend fun fetchCommentList(questionId: Int): Flow<CommentList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchComments(questionId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendComment(questionId: Int, parentId: Int?, comment: String): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendComment(questionId, CommentRequest(parentId, comment))
            }.collect {
                emit(Unit)
            }
        }
    }
}