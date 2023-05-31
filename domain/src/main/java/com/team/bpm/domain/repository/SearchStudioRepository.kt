package com.team.bpm.domain.repository

import com.team.bpm.domain.model.StudioList
import kotlinx.coroutines.flow.Flow

interface SearchStudioRepository {
    suspend fun fetchSearchedStudioList(
        query: String
    ): Flow<StudioList>
}