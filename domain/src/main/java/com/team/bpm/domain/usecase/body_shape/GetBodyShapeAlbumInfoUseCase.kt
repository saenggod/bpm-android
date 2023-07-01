package com.team.bpm.domain.usecase.body_shape

import com.team.bpm.domain.model.BodyShapeSchedule
import com.team.bpm.domain.repository.BodyShapeRepository
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetBodyShapeAlbumInfoUseCase @Inject constructor(private val bodyShapeRepository: BodyShapeRepository) {
    suspend operator fun invoke(albumId : Int): Flow<BodyShapeSchedule> {
        return bodyShapeRepository.fetchAlbumInfo(albumId)
    }
}