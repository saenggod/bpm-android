package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.response.StudioResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.model.Studio
import com.team.bpm.domain.repository.StudioDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class StudioDetailRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : StudioDetailRepository {

    override suspend fun fetchStudioDetail(studioId: Int): Flow<Studio> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchStudioDetail(studioId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }
}