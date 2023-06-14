package com.team.bpm.domain.usecase.user

import com.team.bpm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetUserIdUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: Long): Flow<Long?> {
        return userRepository.setUserId(userId)
    }
}