package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.domain.repository.ScrapRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ScrapRepositoryImpl @Inject constructor(
    private val mainApi: MainApi
) : ScrapRepository {
    override suspend fun sendScrap(studioId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendScrap(studioId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun deleteScrap(studioId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteScrap(studioId)
            }.collect {
                emit(Unit)
            }
        }
    }
}