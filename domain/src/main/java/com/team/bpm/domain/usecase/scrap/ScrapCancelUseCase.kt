package com.team.bpm.domain.usecase.scrap

import com.team.bpm.domain.repository.ScrapRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

class ScrapCancelUseCase @Inject constructor(
    private val scrapRepository: ScrapRepository
) {
    suspend operator fun invoke(studioId: Int): Flow<ResponseBody> {
        return scrapRepository.deleteScrap(studioId)
    }
}