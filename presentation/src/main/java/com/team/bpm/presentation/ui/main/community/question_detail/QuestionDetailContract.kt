package com.team.bpm.presentation.ui.main.community.question_detail

import com.team.bpm.domain.model.Comment
import com.team.bpm.domain.model.Question
import com.team.bpm.presentation.base.BaseContract

interface QuestionDetailContract : BaseContract<QuestionDetailContract.State, QuestionDetailContract.Event, QuestionDetailContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val question: Question? = null,
        val commentList: List<Comment> = emptyList()
    )

    sealed interface Event {
        object GetQuestionDetail : Event
        object GetCommentList : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
    }
}