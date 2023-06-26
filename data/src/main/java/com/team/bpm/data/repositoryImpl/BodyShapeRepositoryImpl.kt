package com.team.bpm.data.repositoryImpl

import com.team.bpm.data.model.request.AlbumRequest
import com.team.bpm.data.model.response.AlbumResponse.Companion.toDataModel
import com.team.bpm.data.model.response.BodyShapeResponse.Companion.toDataModel
import com.team.bpm.data.network.BPMResponseHandlerV2
import com.team.bpm.data.network.MainApi
import com.team.bpm.data.util.convertByteArrayToWebpFile
import com.team.bpm.data.util.createImageMultipartBody
import com.team.bpm.domain.model.Album
import com.team.bpm.domain.model.BodyShape
import com.team.bpm.domain.repository.BodyShapeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class BodyShapeRepositoryImpl @Inject constructor(private val mainApi: MainApi) : BodyShapeRepository {

    override suspend fun sendBodyShape(
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<BodyShape> {
        return flow {
            BPMResponseHandlerV2().handle {
                mainApi.sendBodyShape(
                    content,
                    imageByteArrays.map { imageByteArray ->
                        createImageMultipartBody(
                            key = "file",
                            file = convertByteArrayToWebpFile(imageByteArray)
                        )
                    }
                )
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
}