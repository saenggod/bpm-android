package com.team.bpm.presentation.ui.main.lounge.community.detail

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.Community
import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.model.ReportType

interface CommunityDetailContract : BaseContract<CommunityDetailContract.State, CommunityDetailContract.Event, CommunityDetailContract.Effect> {
    data class State(
        val userId: Long? = null,
        val isLoading: Boolean = false,
        val community: Community? = null,
        val liked: Boolean? = null,
        val likeCount: Int? = null,
        val isCommentListLoading: Boolean = false,
        val commentList: List<Comment> = emptyList(),
        val commentsCount: Int? = 0,
        val redirectCommentId: Int? = null,
        val selectedCommentId: Int? = null,
        val selectedCommentAuthorId: Long? = null,
        val isBottomSheetShowing: Boolean? = null,
        val bottomSheetButtonList: List<BottomSheetButton> = emptyList(),
        val isReportDialogShowing: Boolean = false,
        val reportType: ReportType? = null,
        val isNoticeDialogShowing: Boolean = false,
        val noticeDialogContent: String = ""
    )

    sealed interface Event {
        object GetUserId : Event

        object GetCommunityDetail : Event

        object OnClickCommunityActionButton : Event

        object OnClickDeleteCommunity : Event

        object OnClickReportCommunity : Event

        data class OnClickSendCommunityReport(val reason: String) : Event

        object OnClickLike : Event

        data class OnClickSendComment(val comment: String) : Event

        object GetCommentList : Event

        data class OnClickCommentActionButton(val comment: Comment) : Event

        object OnClickDeleteComment : Event

        object OnClickReportComment : Event

        data class OnClickSendCommentReport(val reason: String) : Event

        data class OnClickCommentLike(val commentId: Int) : Event

        object OnClickDismissReportDialog : Event

        object OnClickDismissNoticeDialog : Event

        object OnClickBackButton : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object RefreshCommentList : Effect

        object ShowKeyboard : Effect

        object GoToCommunityList : Effect
    }
}