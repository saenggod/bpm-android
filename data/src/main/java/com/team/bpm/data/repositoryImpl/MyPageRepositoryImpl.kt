package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.datastore.DataStoreManager
import com.team.bpm.data.model.response.StudioListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.UserScheduleResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponse
import com.team.bpm.data.network.BPMResponseHandler
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.ErrorResponse.Companion.toDataModel
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.model.UserSchedule
import com.team.bpm.domain.repository.MainRepository
import com.team.bpm.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
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