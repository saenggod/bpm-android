package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Studio
import kotlinx.coroutines.flow.Flow

interface StudioDetailRepository {

    suspend fun fetchStudioDetail(
        studioId: Int
    ) : Flow<Studio>
}