package com.team.bpm.domain.repository

import com.team.bpm.domain.model.UserSchedule
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun sendSchedule(
        scheduleName: String,
        studioName: String?,
        date: String,
        time: String?,
        memo: String?
    ): Flow<UserSchedule>

    suspend fun fetchSchedule(scheduleId: Int): Flow<UserSchedule>
}