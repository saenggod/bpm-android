package com.team.bpm.domain.repository

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Studio
import kotlinx.coroutines.flow.Flow

interface StudioDetailRepository {

    suspend fun fetchStudioDetail(
        studioId: Int
    ) : Flow<ResponseState<Studio>>
}