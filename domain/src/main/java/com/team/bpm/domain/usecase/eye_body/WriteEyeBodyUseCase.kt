package com.team.bpm.domain.usecase.eye_body

import com.team.bpm.domain.model.EyeBody
import com.team.bpm.domain.repository.EyeBodyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteEyeBodyUseCase @Inject constructor(private val eyeBodyRepository: EyeBodyRepository) {
    suspend operator fun invoke(
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<EyeBody> {
        return eyeBodyRepository.sendEyeBody(content, imageByteArrays)
    }
}