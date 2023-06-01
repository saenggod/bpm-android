package com.team.bpm.domain.usecase.splash

import com.team.bpm.domain.repository.SplashRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SetKakaoIdUseCase @Inject constructor(
    private val splashRepository: SplashRepository
) {
    suspend operator fun invoke(kakaoId: Long): Flow<Long?> {
    return splashRepository.setKakaoId(kakaoId)
    }
}