package com.team.bpm.presentation.ui.main.lounge.question.detail

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.Question
import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.BottomSheetButton
import com.team.bpm.presentation.model.ReportType

interface QuestionDetailContract : BaseContract<QuestionDetailContract.State, QuestionDetailContract.Event, QuestionDetailContract.Effect> {
    data class State(
        val userId: Long? = null,
        val isLoading: Boolean = false,
        val question: Question? = null,
        val liked: Boolean? = null,
        val likeCount: Int? = null,
        val isCommentListLoading: Boolean = false,
        val commentList: List<Comment> = emptyList(),
        val commentsCount: Int? = 0,
        val redirectCommentId: Int? = null,
        val selectedCommentId: Int? = null,
        val selectedCommentAuthorId: Long? = null,
        val parentCommentId: Int? = null,
        val isBottomSheetShowing: Boolean? = null,
        val bottomSheetButtonList: List<BottomSheetButton> = emptyList(),
        val isReportDialogShowing: Boolean = false,
        val reportType: ReportType? = null,
        val isNoticeDialogShowing: Boolean = false,
        val noticeDialogContent: String = ""
    )

    sealed interface Event {
        object GetUserId : Event

        object GetQuestionDetail : Event

        object OnClickQuestionActionButton : Event

        object OnClickDeleteQuestion : Event

        object OnClickReportQuestion : Event

        data class OnClickSendQuestionReport(val reason: String) : Event

        object OnClickLike : Event

        data class OnClickSendComment(
            val parentId: Int?,
            val comment: String
        ) : Event

        object GetCommentList : Event

        data class OnClickCommentActionButton(
            val comment: Comment,
            val parentCommentId: Int?
        ) : Event

        object OnClickReplyComment : Event

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

        object GoToQuestionList : Effect
    }
}