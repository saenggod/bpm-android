package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Studio
import kotlinx.coroutines.flow.Flow

interface StudioRepository {

    suspend fun fetchStudioDetail(studioId: Int) : Flow<Studio>

    suspend fun sendStudioScrap(studioId: Int): Flow<Unit>

    suspend fun deleteStudioScrap(studioId: Int): Flow<Unit>
}