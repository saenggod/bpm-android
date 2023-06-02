package com.team.bpm.domain.usecase.splash

import com.team.bpm.domain.repository.SplashRepository
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetUserTokenUseCase @Inject constructor(private val splashRepository: SplashRepository) {
    operator fun invoke(): Flow<String?> {
        return splashRepository.getUserToken()
    }
}