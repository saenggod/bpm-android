package com.team.bpm.data.repositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.team.bpm.data.datastore.DataStoreManager
import com.team.bpm.data.model.paging.NotificationPagingSource
import com.team.bpm.data.model.response.UserProfileResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.data.util.convertByteArrayToWebpFile
import com.team.bpm.data.util.createImageMultipartBody
import com.team.bpm.domain.model.Notification
import com.team.bpm.domain.model.UserProfile
import com.team.bpm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val mainApi: MainApi
) : UserRepository {
    override fun loadUserId(): Flow<Long?> {
        return dataStoreManager.getUserId()
    }

    override suspend fun setUserId(userId: Long): Flow<Long?> {
        return dataStoreManager.setUserId(userId)
    }

    override suspend fun sendEditedUserProfile(
        kakaoId: Long,
        nickname: String,
        bio: String,
        imageByteArray: ByteArray
    ): Flow<UserProfile> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendEditedUserProfile(
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

    override suspend fun fetchUserProfile(): Flow<UserProfile> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchUserProfile()
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun fetchNotificationList(): Flow<PagingData<Notification>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory =
            {
                NotificationPagingSource(mainApi = mainApi)
            }
        ).flow
    }

    override suspend fun setNotificationIsRead(notificationId: Long): Flow<Boolean> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.setNotificationIsRead(notificationId)
            }.onEach { result ->
                result.response?.let { emit(true) }
            }.collect()
        }
    }
}