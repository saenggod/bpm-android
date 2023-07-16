package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.request.AlbumRequest
import com.team.bpm.data.model.response.AlbumResponse.Companion.toDataModel
import com.team.bpm.data.model.response.BodyShapeResponse.Companion.toDataModel
import com.team.bpm.data.model.response.BodyShapeScheduleResponse.Companion.toDataModel
import com.team.bpm.data.model.response.BodyShapeSchedulesResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.data.util.convertByteArrayToWebpFile
import com.team.bpm.data.util.createImageMultipartBody
import com.team.bpm.domain.model.Album
import com.team.bpm.domain.model.BodyShape
import com.team.bpm.domain.model.BodyShapeSchedule
import com.team.bpm.domain.model.BodyShapeSchedules
import com.team.bpm.domain.repository.BodyShapeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class BodyShapeRepositoryImpl @Inject constructor(private val mainApi: MainApi) :
    BodyShapeRepository {

    override suspend fun fetchUserSchedule(): Flow<BodyShapeSchedules> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchUserSchedule()
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun fetchAlbumInfo(albumId: Int): Flow<BodyShapeSchedule> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchAlbumInfo(albumId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendAlbum(
        albumName: String,
        date: String,
        memo: String?
    ): Flow<Album> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendAlbum(
                    AlbumRequest(
                        albumName = albumName,
                        date = date,
                        memo = memo
                    )
                )
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendEditedAlbum(
        albumId: Int,
        albumName: String,
        date: String,
        memo: String?
    ): Flow<Album> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendEditedAlbum(
                    albumId = albumId,
                    albumRequest = AlbumRequest(
                        albumName = albumName,
                        date = date,
                        memo = memo
                    )
                )
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun fetchAlbum(albumId: Int): Flow<Album> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchAlbum(albumId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendBodyShape(
        albumId: Int,
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<BodyShape> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendBodyShape(
                    albumId,
                    content,
                    imageByteArrays.map { imageByteArray ->
                        createImageMultipartBody(
                            key = "files",
                            file = convertByteArrayToWebpFile(imageByteArray)
                        )
                    }
                )
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun sendEditedBodyShape(
        albumId: Int,
        bodyShapeId: Int,
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<BodyShape> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendEditedBodyShape(
                    albumId,
                    bodyShapeId,
                    content,
                    imageByteArrays.map { imageByteArray ->
                        createImageMultipartBody(
                            key = "files",
                            file = convertByteArrayToWebpFile(imageByteArray)
                        )
                    }
                )
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun deleteBodyShape(
        albumId: Int,
        bodyShapeId: Int
    ): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteBodyShape(albumId, bodyShapeId)
            }.collect {
                emit(Unit)
            }
        }
    }

    override suspend fun fetchBodyShape(
        albumId: Int,
        bodyShapeId: Int
    ): Flow<BodyShape> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.fetchBodyShape(albumId, bodyShapeId)
            }.onEach { result ->
                result.response?.let { emit(it.toDataModel()) }
            }.collect()
        }
    }

    override suspend fun deleteAlbum(albumId: Int): Flow<Unit> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.deleteUserSchedule(albumId)
            }.collect {
                emit(Unit)
            }
        }
    }
}