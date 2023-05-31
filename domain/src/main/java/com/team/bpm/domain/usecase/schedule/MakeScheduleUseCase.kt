package com.team.bpm.domain.usecase.schedule

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.UserSchedule
import com.team.bpm.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MakeScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {

    suspend operator fun invoke(
        studioName: String,
        date: String,
        time: String,
        memo: String
    ): Flow<UserSchedule> {
        return scheduleRepository.sendSchedule(
            studioName = studioName,
            date = date,
            time = time,
            memo = memo
        )
    }
}