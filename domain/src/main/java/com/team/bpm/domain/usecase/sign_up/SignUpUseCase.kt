package com.team.bpm.domain.usecase.sign_up

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.UserInfo
import com.team.bpm.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
) {
    suspend operator fun invoke(
        kakaoId: Long,
        image: File,
        nickname: String,
        bio: String
    ): Flow<ResponseState<UserInfo>> {
        return signUpRepository.sendSignUp(
            kakaoId = kakaoId,
            image = image,
            nickname = nickname,
            bio = bio
        )
    }
}