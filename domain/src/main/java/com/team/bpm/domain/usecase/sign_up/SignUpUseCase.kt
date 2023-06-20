package com.team.bpm.domain.usecase.sign_up

import com.team.bpm.domain.model.UserProfile
import com.team.bpm.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val signUpRepository: SignUpRepository) {
    suspend operator fun invoke(
        kakaoId: Long,
        imageByteArray: ByteArray,
        nickname: String,
        bio: String
    ): Flow<UserProfile> {
        return signUpRepository.sendSignUp(
            kakaoId = kakaoId,
            imageByteArray = imageByteArray,
            nickname = nickname,
            bio = bio
        )
    }
}