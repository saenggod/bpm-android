package com.team.bpm.domain.usecase.schedule

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Schedule
import com.team.bpm.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {

    suspend operator fun invoke(): Flow<ResponseState<Schedule>> {
        return scheduleRepository.fetchSchedule()
    }
}