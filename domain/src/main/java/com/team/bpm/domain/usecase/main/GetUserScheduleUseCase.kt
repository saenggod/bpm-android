package com.team.bpm.domain.usecase.main

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.UserSchedule
import com.team.bpm.domain.repository.HomeRepository
import dagger.Reusable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@Reusable
class GetUserScheduleUseCase @Inject constructor(private val mainRepository: HomeRepository) {
    suspend operator fun invoke(): Flow<ResponseState<UserSchedule>> {
        return mainRepository.fetchUserSchedule()
    }
}