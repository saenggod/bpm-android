package com.team.bpm.domain.usecase.community

import com.team.bpm.domain.model.CommentList
import com.team.bpm.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommunityCommentListUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(communityId: Int): Flow<CommentList> {
        return communityRepository.fetchCommunityCommentList(communityId)
    }
}