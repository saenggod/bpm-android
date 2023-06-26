package com.team.bpm.domain.usecase.body_shape

import com.team.bpm.domain.model.BodyShape
import com.team.bpm.domain.repository.BodyShapeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditBodyShapeUseCase @Inject constructor(private val bodyShapeRepository: BodyShapeRepository) {
    suspend operator fun invoke(
        albumId: Int,
        bodyShapeId: Int,
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<BodyShape> {
        return bodyShapeRepository.sendEditedBodyShape(albumId, bodyShapeId, content, imageByteArrays)
    }
}