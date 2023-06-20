package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.datastore.DataStoreManager
import com.team.bpm.data.model.request.UserVerificationRequest
import com.team.bpm.data.model.response.UserProfileResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.UserProfile
import com.team.bpm.domain.repository.SplashRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class SplashRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val mainApi: MainApi
) : SplashRepository {

    override fun getKakaoId(): Flow<Long?> {
        return dataStoreManager.getKakaoId()
    }

    override suspend fun setKakaoId(kakaoId: Long): Flow<Long?> {
        return dataStoreManager.setKakaoId(kakaoId)
    }

    override fun getUserToken(): Flow<String?> {
        return dataStoreManager.getUserToken()
    }

    override suspend fun setUserToken(userToken: String): Flow<String?> {
        return dataStoreManager.setUserToken(userToken)
    }

    override suspend fun sendSignIn(kakaoId: Long): Flow<UserProfile> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendKakaoIdVerification(UserVerificationRequest(kakaoId))
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }
}