package com.team.bpm.domain.usecase.making_album

import com.team.bpm.domain.model.Album
import com.team.bpm.domain.repository.BodyShapeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MakeAlbumUseCase @Inject constructor(private val bodyShapeRepository: BodyShapeRepository) {
    suspend operator fun invoke(
        albumName: String,
        date: String,
        memo: String?
    ): Flow<Album> {
        return bodyShapeRepository.sendAlbum(
            albumName = albumName,
            date = date,
            memo = memo
        )
    }
}