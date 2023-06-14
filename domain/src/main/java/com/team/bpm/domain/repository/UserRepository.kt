package com.team.bpm.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserId(): Flow<Long?>

    suspend fun setUserId(userId: Long): Flow<Long?>
}