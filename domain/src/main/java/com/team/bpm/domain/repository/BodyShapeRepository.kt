package com.team.bpm.domain.repository

import com.team.bpm.domain.model.BodyShape
import com.team.bpm.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface BodyShapeRepository {
    suspend fun sendBodyShape(
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<BodyShape>

    suspend fun sendAlbum(
        albumName: String,
        date: String,
        memo: String?
    ): Flow<Album>

    suspend fun sendEditedAlbum(
        albumId: Int,
        albumName: String,
        date: String,
        memo: String?
    ): Flow<Album>

    suspend fun fetchAlbum(albumId: Int): Flow<Album>
}