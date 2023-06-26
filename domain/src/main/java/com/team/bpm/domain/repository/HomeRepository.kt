package com.team.bpm.domain.repository

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun fetchStudioList(
        limit: Int,
        offset: Int,
        type : String
    ): Flow<StudioList>

    suspend fun fetchAlbum(): Flow<ResponseState<Album>>

}