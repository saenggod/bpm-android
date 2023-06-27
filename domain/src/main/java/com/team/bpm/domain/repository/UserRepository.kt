package com.team.bpm.domain.repository

import com.team.bpm.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun loadUserId(): Flow<Long?>

    suspend fun setUserId(userId: Long): Flow<Long?>

    suspend fun sendEditedUserProfile(
        kakaoId: Long,
        nickname: String,
        bio: String,
        imageByteArray: ByteArray
    ): Flow<Unit>

    suspend fun fetchUserProfile(): Flow<UserProfile>
}