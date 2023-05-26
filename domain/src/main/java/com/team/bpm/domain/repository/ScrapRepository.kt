package com.team.bpm.domain.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface ScrapRepository {
    suspend fun sendScrap(studioId: Int): Flow<ResponseBody>
    suspend fun deleteScrap(studioId: Int): Flow<ResponseBody>
}