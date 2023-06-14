package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.datastore.DataStoreManager
import com.team.bpm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val dataStoreManager: DataStoreManager) : UserRepository {
    override fun getUserId(): Flow<Long?> {
        return dataStoreManager.getUserId()
    }

    override suspend fun setUserId(userId: Long): Flow<Long?> {
        return dataStoreManager.setUserId(userId)
    }
}