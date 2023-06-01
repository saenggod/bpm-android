package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.datastore.DataStoreManager
import com.team.bpm.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : MyPageRepository {
    override fun getMainTabIndex(): Flow<Int?> {
        return dataStoreManager.getStartTabIndex()
    }

    override suspend fun setMainTabIndex(index: Int): Flow<Int?> {
        return dataStoreManager.setStartTabIndex(index)
    }
}