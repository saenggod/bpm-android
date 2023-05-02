package com.team.bpm.domain.repository

import android.graphics.Bitmap
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface SignUpRepository {

    suspend fun sendSignUp(
        kakaoId: Long,
        nickname: String,
        bio: String,
        image: Bitmap
    ): Flow<ResponseState<UserInfo>>
}