package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.SignUpResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponse
import com.team.bpm.data.network.BPMResponseHandler
import com.team.bpm.data.network.ErrorResponse.Companion.toDataModel
import com.team.bpm.data.network.MainApi
import com.team.bpm.data.util.convertByteArrayToWebpFile
import com.team.bpm.data.util.createImageMultipartBody
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.UserInfo
import com.team.bpm.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : SignUpRepository {
    override suspend fun sendSignUp(
        kakaoId: Long,
        nickname: String,
        bio: String,
        imageByteArray: ByteArray
    ): Flow<ResponseState<UserInfo>> {
        return flow {
            BPMResponseHandler().handle {
                mainApi.signUp(
                    kakaoId = kakaoId,
                    nickname = nickname,
                    bio = bio,
                    file = createImageMultipartBody(
                        key = "file",
                        file = convertByteArrayToWebpFile(imageByteArray)
                    )
                )
            }.onEach { result ->
                when (result) {
                    is BPMResponse.Success -> emit(ResponseState.Success(result.data.toDataModel()))
                    is BPMResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }
}