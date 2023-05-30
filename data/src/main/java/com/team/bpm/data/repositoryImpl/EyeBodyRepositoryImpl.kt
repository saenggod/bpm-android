package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.EyeBodyResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.data.util.convertByteArrayToWebpFile
import com.team.bpm.data.util.createImageMultipartBody
import com.team.bpm.domain.model.EyeBody
import com.team.bpm.domain.repository.EyeBodyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class EyeBodyRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : EyeBodyRepository {
    override suspend fun sendEyeBody(content: String, imageByteArrays: List<ByteArray>): Flow<EyeBody> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendEyeBody(
                    content,
                    imageByteArrays.map { imageByteArray ->
                        createImageMultipartBody(
                            key = "file",
                            file = convertByteArrayToWebpFile(imageByteArray)
                        )
                    }
                )
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }
}