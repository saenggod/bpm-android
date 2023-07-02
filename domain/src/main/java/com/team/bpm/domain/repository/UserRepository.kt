package com.team.bpm.domain.repository

import androidx.paging.PagingData
import com.team.bpm.domain.model.Notification
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
    ): Flow<UserProfile>

    suspend fun fetchUserProfile(): Flow<UserProfile>

    suspend fun fetchNotificationList(): Flow<PagingData<Notification>>

    suspend fun setNotificationIsRead(notificationId: Long): Flow<Boolean>
}