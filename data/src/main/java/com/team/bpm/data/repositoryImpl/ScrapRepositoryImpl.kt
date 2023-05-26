package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.repository.ScrapRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody
import javax.inject.Inject

class ScrapRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : ScrapRepository {
    override suspend fun sendScrap(studioId: Int): Flow<ResponseBody> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendScrap(studioId)
            }.onEach { result ->
                result.response?.let { emit(it) }
                println(result)
                println(result.response)
            }.collect()
        }
    }

    override suspend fun deleteScrap(studioId: Int): Flow<ResponseBody> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteScrap(studioId)
            }.onEach { result ->
                result.response?.let { emit(it) }
                println(result)
                println(result.response)
            }.collect()
        }
    }
}