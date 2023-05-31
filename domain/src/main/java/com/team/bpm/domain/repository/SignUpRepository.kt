package com.team.bpm.domain.repository

import com.team.bpm.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface SignUpRepository {

    suspend fun sendSignUp(
        kakaoId: Long,
        nickname: String,
        bio: String,
        imageByteArray: ByteArray
    ): Flow<UserInfo>
}