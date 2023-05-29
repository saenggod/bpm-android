package com.team.bpm.domain.usecase.mypage

import com.team.bpm.domain.repository.MyPageRepository
import com.team.bpm.domain.repository.SplashRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SetMainTabIndexUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(index: Int): Flow<Int?> {
        return myPageRepository.setMainTabIndex(index)
    }
}