package com.team.bpm.presentation.ui.main.studio.search

import com.team.bpm.presentation.base.BaseContract

interface SearchContract : BaseContract<SearchContract.State, SearchContract.Event, SearchContract.Effect> {
    data class State(
        val isRecentSearchListLoading: Boolean = false,
        val recentSearchList: List<String> = emptyList()
    )

    sealed interface Event {
        object OnClickBackButton : Event
        object GetRecentSearchList : Event

        data class Search(val text: String, val shouldBeSaved: Boolean) : Event

        data class OnClickDeleteRecentSearch(val index: Int) : Event
    }

    sealed interface Effect {
        object GoBack : Effect

        data class ShowToast(val text: String) : Effect

        object EraseSearch : Effect

        data class GoToSearchResult(val search: String) : Effect
    }
}