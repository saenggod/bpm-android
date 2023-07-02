package com.team.bpm.domain.usecase.user

import androidx.paging.PagingData
import com.team.bpm.domain.model.Notification
import com.team.bpm.domain.repository.UserRepository
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetNotificationUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<PagingData<Notification>> {
        return userRepository.fetchNotificationList()
    }
}