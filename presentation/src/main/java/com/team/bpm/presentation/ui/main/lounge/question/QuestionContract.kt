package com.team.bpm.presentation.ui.main.lounge.question

import com.team.bpm.domain.model.Question
import com.team.bpm.presentation.base.BaseContract

interface QuestionContract :
    BaseContract<QuestionContract.State, QuestionContract.Event, QuestionContract.Effect> {
    data class State(
        val questionList: List<Question>? = null
    )

    sealed interface Event {
        data class OnClickListItem(val questionId: Int) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object GoToTop : Effect
        data class GoToQuestionDetail(val questionId: Int) : Effect
    }
}