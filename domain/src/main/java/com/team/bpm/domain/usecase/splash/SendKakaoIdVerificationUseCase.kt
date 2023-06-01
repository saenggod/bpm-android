package com.team.bpm.domain.usecase.splash

import com.team.bpm.domain.model.UserInfo
import com.team.bpm.domain.repository.SplashRepository
import dagger.Reusable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@Reusable
class SendKakaoIdVerificationUseCase @Inject constructor(
    private val splashRepository: SplashRepository
) {
    suspend operator fun invoke(kakaoId: Long): Flow<UserInfo> {
        return splashRepository.sendSignIn(kakaoId)
    }
}