package com.team.bpm.domain.usecase.question

import com.team.bpm.domain.model.CommunityList
import com.team.bpm.domain.model.QuestionList
import com.team.bpm.domain.repository.CommunityRepository
import com.team.bpm.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyPostListUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(page: Int, size: Int): Flow<CommunityList> {
        return communityRepository.fetchMyPostList(page, size)
    }
}