package com.team.bpm.domain.usecase.user

import com.team.bpm.domain.model.UserProfile
import com.team.bpm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<UserProfile> {
        return userRepository.fetchUserProfile()
    }
}