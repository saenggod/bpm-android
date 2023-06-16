package com.team.bpm.presentation.ui.main.mypage.myquestion.more

import com.team.bpm.presentation.base.BaseContract

interface MyQuestionMoreContract :
    BaseContract<MyQuestionMoreContract.State, MyQuestionMoreContract.Event, MyQuestionMoreContract.Effect> {
    data class State(
        val isLoading: Boolean? = null
    )

    sealed interface Event {
        object Delete : Event
    }

    sealed interface Effect {
        object GoDelete : Effect
    }
}