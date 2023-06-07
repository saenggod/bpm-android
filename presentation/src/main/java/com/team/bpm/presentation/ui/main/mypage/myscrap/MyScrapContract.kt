package com.team.bpm.presentation.ui.main.mypage.myscrap

import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.base.BaseContract

interface MyScrapContract : BaseContract<MyScrapContract.State, MyScrapContract.Event, MyScrapContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val myScrapList: List<Studio> = emptyList(),
        val scrapCount: Int? = 0
    )

    sealed interface Event {
        object GetMyScrapList : Event

        data class OnClickStudio(val studioId: Int) : Event

        data class OnClickCancelScrap(val studioId: Int) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object RefreshMyScrapList : Effect

        data class GoToStudioDetail(val studioId: Int) : Effect
    }
}