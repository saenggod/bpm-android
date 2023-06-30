package com.team.bpm.domain.usecase.user

import com.team.bpm.domain.model.UserProfile
import com.team.bpm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditUserProfileUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        kakaoId: Long,
        nickname: String,
        bio: String,
        imageByteArray: ByteArray
    ): Flow<UserProfile> {
        return userRepository.sendEditedUserProfile(kakaoId, nickname, bio, imageByteArray)
    }
}