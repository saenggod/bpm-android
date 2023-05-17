package com.team.bpm.domain.repository

import com.team.bpm.domain.model.ResponseState
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface ScrapRepository {
    suspend fun sendScrap(studioId: Int): Flow<ResponseState<ResponseBody>>
    suspend fun deleteScrap(studioId: Int): Flow<ResponseState<ResponseBody>>
}