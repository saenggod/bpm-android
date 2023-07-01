package com.team.bpm.domain.repository

import com.team.bpm.domain.model.Album
import com.team.bpm.domain.model.BodyShape
import com.team.bpm.domain.model.BodyShapeSchedule
import com.team.bpm.domain.model.BodyShapeSchedules
import kotlinx.coroutines.flow.Flow

interface BodyShapeRepository {

    suspend fun fetchUserSchedule() : Flow<BodyShapeSchedules>

    suspend fun fetchAlbumInfo(albumId: Int): Flow<BodyShapeSchedule>

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

    suspend fun sendBodyShape(
        albumId: Int,
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<BodyShape>

    suspend fun sendEditedBodyShape(
        albumId: Int,
        bodyShapeId: Int,
        content: String,
        imageByteArrays: List<ByteArray>
    ): Flow<BodyShape>

    suspend fun deleteBodyShape(
        albumId: Int,
        bodyShapeId: Int
    ): Flow<Unit>

    suspend fun fetchBodyShape(
        albumId: Int,
        bodyShapeId: Int
    ): Flow<BodyShape>
}