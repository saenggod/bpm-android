package com.team.bpm.domain.usecase.user

import com.team.bpm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<Long?> {
        return userRepository.getUserId()
    }
}