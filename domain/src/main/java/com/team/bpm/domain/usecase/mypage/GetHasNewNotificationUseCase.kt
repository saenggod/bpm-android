package com.team.bpm.domain.usecase.mypage

import com.team.bpm.domain.repository.MyPageRepository
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetHasNewNotificationUseCase @Inject constructor(private val myPageRepository: MyPageRepository) {
    suspend operator fun invoke(): Flow<Boolean> {
        return myPageRepository.fetchIsNewNotification()
    }
}