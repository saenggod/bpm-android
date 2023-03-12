package com.team.bpm.domain.usecase.schedule

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.Schedule
import com.team.bpm.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {

    suspend operator fun invoke(
        studioName: String,
        date: String,
        time: String,
        memo: String
    ): Flow<ResponseState<Schedule>> {
        return scheduleRepository.sendSchedule(
            studioName = studioName,
            date = date,
            time = time,
            memo = memo
        )
    }
}