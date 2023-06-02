package com.team.bpm.presentation.ui.main.community.question_detail

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.Question
import com.team.bpm.presentation.base.BaseContract
import com.team.bpm.presentation.model.BottomSheetButton

interface QuestionDetailContract : BaseContract<QuestionDetailContract.State, QuestionDetailContract.Event, QuestionDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val isCommentListLoading: Boolean = false,
        val userId: Long? = null,
        val question: Question? = null,
        val commentList: List<Comment> = emptyList(),
        val commentsCount: Int? = 0,
        val redirectCommentId: Int? = null,
        val selectedCommentId: Int? = null,
        val selectedCommentAuthorId: Int? = null,
        val parentCommentId: Int? = null,
        val liked: Boolean? = null,
        val likeCount: Int? = null,
        val bottomSheetButtonList: List<BottomSheetButton> = emptyList(),
        val isReportDialogShowing: Boolean = false,
        val isNoticeDialogShowing: Boolean = false,
        val noticeDialogContent: String = ""
    )

    sealed interface Event {
        object GetUserId : Event

        object GetQuestionDetail : Event

        object GetCommentList : Event

        object OnClickDeleteQuestion : Event

        object OnClickReportQuestion : Event

        data class OnClickSendQuestionReport(val reason: String) : Event

        data class OnClickSendComment(
            val parentId: Int?,
            val comment: String
        ) : Event

        data class OnClickCommentActionButton(
            val commentId: Int,
            val authorId: Int,
            val parentCommentId: Int?
        ) : Event

        object OnClickReplyComment : Event

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

        object ShowKeyboard : Effect

        object GoToQuestionList : Effect
    }
}