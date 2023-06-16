package com.team.bpm.presentation.ui.main.search.search_result

import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.base.BaseContract

interface SearchResultContract : BaseContract<SearchResultContract.State, SearchResultContract.Event, SearchResultContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val studioList: List<Studio> = emptyList(),
        val isFiltering: Boolean = false,
        val isFiltered: Boolean = false,
        val filteredRegion: String? = null,
        val filteredKeywordList: List<String> = emptyList()
    )

    sealed interface Event {
        object GetSearchResult : Event

        data class OnClickSearch(val search: String) : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect
    }
}