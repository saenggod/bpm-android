package com.team.bpm.domain.usecase.making_album

import com.team.bpm.domain.model.Album
import com.team.bpm.domain.repository.BodyShapeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(private val bodyShapeRepository: BodyShapeRepository) {
    suspend operator fun invoke(albumId: Int): Flow<Album> {
        return bodyShapeRepository.fetchAlbum(albumId)
    }
}