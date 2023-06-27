package com.team.bpm.domain.usecase.body_shape

import com.team.bpm.domain.model.BodyShapeSchedules
import com.team.bpm.domain.repository.BodyShapeRepository
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetBodyShapeSchedulesUseCase @Inject constructor(private val bodyShapeRepository: BodyShapeRepository) {
    suspend operator fun invoke(): Flow<BodyShapeSchedules> {
        return bodyShapeRepository.fetchUserSchedule()
    }
}