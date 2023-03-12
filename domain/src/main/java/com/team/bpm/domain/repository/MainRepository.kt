package com.team.bpm.domain.repository

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.model.UserSchedule
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    // TODO : Move to HomeRepository
    suspend fun getStudioList(limit: Int, offset: Int): Flow<ResponseState<StudioList>>

    suspend fun getUserSchedule(): Flow<ResponseState<UserSchedule>>

}