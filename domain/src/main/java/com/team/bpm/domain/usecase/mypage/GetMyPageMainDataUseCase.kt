package com.team.bpm.domain.usecase.mypage

import com.team.bpm.domain.model.UserProfile
import com.team.bpm.domain.repository.MyPageRepository
import com.team.bpm.domain.repository.UserRepository
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

@Reusable
class GetMyPageMainDataUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<Pair<UserProfile, Boolean>> {
        return userRepository.fetchUserProfile().zip(
            myPageRepository.fetchIsNewNotification()
        ) { userProfile, isNewNotification ->
            userProfile to isNewNotification
        }
    }
}