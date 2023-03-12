package com.team.bpm.domain.repository

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Schedule
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun fetchSchedule(): Flow<ResponseState<Schedule>>

    suspend fun sendSchedule(
        studioName: String,
        date: String,
        time: String,
        memo: String
    ): Flow<ResponseState<Schedule>>
}