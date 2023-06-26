package com.team.bpm.domain.usecase.eye_body

import com.team.bpm.domain.model.BodyShape
import com.team.bpm.domain.repository.BodyShapeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteEyeBodyUseCase @Inject constructor(private val bodyShapeRepository: BodyShapeRepository) {
    suspend operator fun invoke(
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<BodyShape> {
        return bodyShapeRepository.sendBodyShape(content, imageByteArrays)
    }
}