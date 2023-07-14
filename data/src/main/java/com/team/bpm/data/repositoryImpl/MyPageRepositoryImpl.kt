package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.datastore.DataStoreManager
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val mainApi: MainApi
) : MyPageRepository {
    override fun getMainTabIndex(): Flow<Int?> {
        return dataStoreManager.getStartTabIndex()
    }

    override suspend fun setMainTabIndex(index: Int): Flow<Int?> {
        return dataStoreManager.setStartTabIndex(index)
    }

    override suspend fun fetchIsNewNotification(): Flow<Boolean> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchNotificationList(0)
            }.onEach { result ->
                var hasNewAlarm = false

                result.response?.alarmResponseList?.let {
                    hasNewAlarm = (it.none { it.read == true }) && it.isNotEmpty()
                }

                result.response?.let { emit(hasNewAlarm) }
            }.collect()
        }
    }
}