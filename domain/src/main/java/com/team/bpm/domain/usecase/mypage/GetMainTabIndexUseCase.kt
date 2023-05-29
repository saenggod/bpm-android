package com.team.bpm.domain.usecase.mypage

import com.team.bpm.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMainTabIndexUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    operator fun invoke(): Flow<Int?> {
        return myPageRepository.getMainTabIndex()
    }
}