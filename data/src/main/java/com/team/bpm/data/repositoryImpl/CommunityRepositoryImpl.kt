package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.request.CommentRequest
import com.team.bpm.data.model.response.CommentListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.CommentResponse.Companion.toDataModel
import com.team.bpm.data.model.response.CommunityResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.CommentList
import com.team.bpm.domain.model.Community
import com.team.bpm.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : CommunityRepository {
    override suspend fun fetchCommunityDetail(communityId: Int): Flow<Community> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchCommunityDetail(communityId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun fetchCommunityCommentList(communityId: Int): Flow<CommentList> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchCommunityComments(communityId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendCommunityComment(communityId: Int, parentId: Int?, comment: String): Flow<Comment> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendCommunityComment(communityId, CommentRequest(parentId, comment))
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendCommunityLike(communityId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendCommunityLike(communityId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun deleteCommunityLike(communityId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteCommunityLike(communityId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun sendCommunityCommentLike(communityId: Int, commentId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendCommunityCommentLike(communityId, commentId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun deleteCommunityCommentLike(communityId: Int, commentId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteCommunityCommentLike(communityId, commentId)
            }.collect {
                emit(Unit)
            }
        }
    }
}