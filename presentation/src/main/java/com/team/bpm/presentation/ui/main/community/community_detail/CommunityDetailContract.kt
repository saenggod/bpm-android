package com.team.bpm.presentation.ui.main.community.community_detail

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.Community
import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.model.ReportType

interface CommunityDetailContract : BaseContract<CommunityDetailContract.State, CommunityDetailContract.Event, CommunityDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val isCommentListLoading: Boolean = false,
        val userId: Long? = null,
        val community: Community? = null,
        val commentList: List<Comment> = emptyList(),
        val commentsCount: Int? = 0,
        val redirectCommentId: Int? = null,
        val selectedCommentId: Int? = null,
        val selectedCommentAuthorId: Long? = null,
        val liked: Boolean? = null,
        val likeCount: Int? = null,
        val bottomSheetButtonList: List<BottomSheetButton> = emptyList(),
        val reportType: ReportType? = null,
        val isReportDialogShowing: Boolean = false,
        val isNoticeDialogShowing: Boolean = false,
        val noticeDialogContent: String = ""
    )

    sealed interface Event {
        object GetUserId : Event

        object GetCommunityDetail : Event

        object GetCommentList : Event

        object OnClickCommunityActionButton : Event

        object OnClickDeleteCommunity : Event

        object OnClickReportCommunity : Event

        data class OnClickSendCommunityReport(val reason: String) : Event

        data class OnClickSendComment(val comment: String) : Event

        data class OnClickCommentActionButton(
            val commentId: Int,
            val authorId: Long,
            val parentCommentId: Int?
        ) : Event

        object OnClickDeleteComment : Event

        object OnClickReportComment : Event

        object OnClickDismissReportDialog : Event

        object OnClickDismissNoticeDialog : Event

        object OnClickLike : Event

        data class OnClickCommentLike(val commentId: Int) : Event

        data class OnClickSendCommentReport(val reason: String) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object RefreshCommentList : Effect

        object ExpandBottomSheet : Effect

        object CollapseBottomSheet : Effect

        object ShowKeyboard : Effect

        object GoToCommunityList : Effect
    }
}