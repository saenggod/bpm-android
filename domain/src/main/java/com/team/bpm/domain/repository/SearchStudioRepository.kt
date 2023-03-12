package com.team.bpm.domain.repository

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.StudioList
import kotlinx.coroutines.flow.Flow

interface SearchStudioRepository {
    suspend fun fetchSearchStudioResult(
        query: String
    ) : Flow<ResponseState<StudioList>>
}