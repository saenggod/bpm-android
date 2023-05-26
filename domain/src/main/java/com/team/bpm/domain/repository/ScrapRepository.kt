package com.team.bpm.domain.repository

import kotlinx.coroutines.flow.Flow

interface ScrapRepository {
    suspend fun sendScrap(studioId: Int): Flow<Unit>
    suspend fun deleteScrap(studioId: Int): Flow<Unit>
}