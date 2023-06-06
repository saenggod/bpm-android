package com.team.bpm.domain.usecase.schedule

import com.team.bpm.domain.model.UserSchedule
import com.team.bpm.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditScheduleUseCase @Inject constructor(private val scheduleRepository: ScheduleRepository) {
    suspend operator fun invoke(
        scheduleId: Int,
        scheduleName: String,
        studioName: String?,
        date: String,
        time: String?,
        memo: String?
    ): Flow<UserSchedule> {
        return scheduleRepository.sendEditedSchedule(
            scheduleId = scheduleId,
            scheduleName = scheduleName,
            studioName = studioName,
            date = date,
            time = time,
            memo = memo
        )
    }
}