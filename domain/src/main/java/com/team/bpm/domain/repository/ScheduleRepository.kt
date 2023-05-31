package com.team.bpm.domain.repository

import com.team.bpm.domain.model.UserSchedule
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun sendSchedule(
        studioName: String,
        date: String,
        time: String,
        memo: String
    ): Flow<UserSchedule>

    suspend fun fetchSchedule(): Flow<UserSchedule>
}