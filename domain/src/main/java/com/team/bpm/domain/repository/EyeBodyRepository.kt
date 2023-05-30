package com.team.bpm.domain.repository

import com.team.bpm.domain.model.EyeBody
import kotlinx.coroutines.flow.Flow

interface EyeBodyRepository {
    suspend fun sendEyeBody(content: String, imageByteArrays: List<ByteArray>): Flow<EyeBody>
}