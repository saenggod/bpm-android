package com.team.bpm.presentation.ui.main.mypage.myquestion

import com.team.bpm.domain.model.Question
import com.team.bpm.presentation.base.BaseContract

interface MyQuestionContract :
    BaseContract<MyQuestionContract.State, MyQuestionContract.Event, MyQuestionContract.Effect> {
    data class State(
        val isDeleteMode: Boolean = false,
        val isAllSelected: Boolean = false,
        val questionList: List<Question>? = null
    )

    sealed interface Event {
        data class OnClickListItem(val questionId: Int) : Event
        object OnClickMore : Event
        object OnBackPressed : Event
        object OnClickDeleteMode : Event
        object OnClickItemDelete : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
        object ShowDeleteBottomSheet : Effect
        data class GoToQuestionDetail(val questionId: Int) : Effect
        object GoOut : Effect
    }
}