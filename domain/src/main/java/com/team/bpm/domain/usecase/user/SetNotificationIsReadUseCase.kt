package com.team.bpm.domain.usecase.user

import com.team.bpm.domain.repository.UserRepository
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class SetNotificationIsReadUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(notificationId: Long): Flow<Boolean> {
        return userRepository.setNotificationIsRead(notificationId)
    }
}