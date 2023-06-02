package com.team.bpm.presentation.ui.main.community.community_detail

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.Community
import com.team.bpm.presentation.base.BaseContract

interface CommunityDetailContract : BaseContract<CommunityDetailContract.State, CommunityDetailContract.Event, CommunityDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val community: Community? = null,
        val commentList: List<Comment>? = null,
        val commentsCount: Int? = 0,
        val redirectCommentId: Int? = null,
        val selectedCommentId: Int? = null,
        val parentCommentId: Int? = null,
        val liked: Boolean? = null,
        val likeCount: Int? = null
    )

    sealed interface Event {
        object GetCommunityDetail : Event

        object GetCommentList : Event

        data class OnClickSendComment(val comment: String) : Event

        data class OnClickCommentActionButton(val commentId: Int) : Event

        object OnClickLike : Event

        data class OnClickCommentLike(val commentId: Int) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object RefreshCommentList : Effect

        object ExpandBottomSheet : Effect

        object ShowKeyboard : Effect
    }
}