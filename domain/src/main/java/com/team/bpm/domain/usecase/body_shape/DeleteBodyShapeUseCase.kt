package com.team.bpm.domain.usecase.body_shape

import com.team.bpm.domain.repository.BodyShapeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteBodyShapeUseCase @Inject constructor(private val bodyShapeRepository: BodyShapeRepository) {
    suspend operator fun invoke(
        albumId: Int,
        bodyShapeId: Int
    ): Flow<Unit> {
        return bodyShapeRepository.deleteBodyShape(albumId, bodyShapeId)
    }
}