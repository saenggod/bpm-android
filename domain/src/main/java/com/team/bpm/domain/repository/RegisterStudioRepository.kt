package com.team.bpm.domain.repository

import com.team.bpm.domain.model.RegisterStudioWrapper
import com.team.bpm.domain.model.ResponseState
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface RegisterStudioRepository {
    suspend fun sendStudio(registerStudioWrapper: RegisterStudioWrapper): Flow<ResponseState<ResponseBody>>
}