package com.team.bpm.domain.repository

import com.team.bpm.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserId(): Flow<Long?>

    suspend fun setUserId(userId: Long): Flow<Long?>

    suspend fun getUserProfile(): Flow<UserProfile>
}