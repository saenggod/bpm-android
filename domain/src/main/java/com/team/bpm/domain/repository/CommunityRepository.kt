package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.CommentList
import com.team.bpm.domain.model.Community
import com.team.bpm.domain.model.CommunityList
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    suspend fun fetchCommunityList(page: Int, size: Int): Flow<CommunityList>

    suspend fun sendCommunity(
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<Community>

    suspend fun deleteCommunity(communityId: Int): Flow<Unit>

    suspend fun fetchCommunityDetail(communityId: Int): Flow<Community>

    suspend fun sendCommunityReport(
        communityId: Int,
        reason: String
    ): Flow<Unit>

    suspend fun sendCommunityLike(communityId: Int): Flow<Unit>

    suspend fun deleteCommunityLike(communityId: Int): Flow<Unit>

    suspend fun sendCommunityComment(
        communityId: Int,
        comment: String
    ): Flow<Comment>

    suspend fun fetchCommunityCommentList(communityId: Int): Flow<CommentList>

    suspend fun deleteCommunityComment(
        communityId: Int,
        commentId: Int
    ): Flow<Unit>

    suspend fun sendCommunityCommentReport(
        communityId: Int,
        commentId: Int,
        reason: String
    ): Flow<Unit>

    suspend fun sendCommunityCommentLike(
        communityId: Int,
        commentId: Int
    ): Flow<Unit>

    suspend fun deleteCommunityCommentLike(
        communityId: Int,
        commentId: Int
    ): Flow<Unit>
}