package com.team.bpm.domain.usecase.splash

import com.team.bpm.domain.model.UserInfo
import com.team.bpm.domain.repository.SplashRepository
import dagger.Reusable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@Reusable
class SendKakaoUserIdVerificationUseCase @Inject constructor(
    private val splashRepository: SplashRepository
) {
    suspend operator fun invoke(kakaoUserId: Long): Flow<UserInfo> {
        return splashRepository.sendSignIn(kakaoUserId)
    }
}