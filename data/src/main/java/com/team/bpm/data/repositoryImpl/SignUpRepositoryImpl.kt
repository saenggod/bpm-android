package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.UserProfileResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.data.util.convertByteArrayToWebpFile
import com.team.bpm.data.util.createImageMultipartBody
import com.team.bpm.domain.model.UserProfile
import com.team.bpm.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(private val mainApi: MainApi) : SignUpRepository {
    override suspend fun sendSignUp(
        kakaoId: Long,
        nickname: String,
        bio: String,
        imageByteArray: ByteArray
    ): Flow<UserProfile> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendSignUp(
                    kakaoId = kakaoId,
                    nickname = nickname,
                    bio = bio,
                    file = createImageMultipartBody(
                        key = "file",
                        file = convertByteArrayToWebpFile(imageByteArray)
                    )
                )
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }
}