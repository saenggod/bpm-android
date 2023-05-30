package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.CommentList
import com.team.bpm.domain.model.Community
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    suspend fun sendCommunity(content: String, imageByteArrays: List<ByteArray>): Flow<Community>
    suspend fun fetchCommunityDetail(communityId: Int): Flow<Community>
    suspend fun fetchCommunityCommentList(communityId: Int): Flow<CommentList>
    suspend fun sendCommunityComment(communityId: Int, parentId: Int?, comment: String): Flow<Comment>
    suspend fun sendCommunityLike(communityId: Int): Flow<Unit>
    suspend fun deleteCommunityLike(communityId: Int): Flow<Unit>
    suspend fun sendCommunityCommentLike(communityId: Int, commentId: Int): Flow<Unit>
    suspend fun deleteCommunityCommentLike(communityId: Int, commentId: Int): Flow<Unit>
}