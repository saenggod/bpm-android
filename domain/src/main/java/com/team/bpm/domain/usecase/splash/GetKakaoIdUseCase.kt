package com.team.bpm.domain.usecase.splash

import com.team.bpm.domain.repository.SplashRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetKakaoIdUseCase @Inject constructor(private val splashRepository: SplashRepository) {
    operator fun invoke(): Flow<Long?> {
        return splashRepository.getKakaoId()
    }
}