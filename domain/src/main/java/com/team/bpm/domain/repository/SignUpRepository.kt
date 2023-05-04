package com.team.bpm.domain.repository

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow
import java.io.File

interface SignUpRepository {

    suspend fun sendSignUp(
        kakaoId: Long,
        nickname: String,
        bio: String,
        imageByteArray: ByteArray
    ): Flow<ResponseState<UserInfo>>
}

// TODO : 다른 UseCase 에 있는 <File> 도 <ByteArray> 로 변경 되어야 함