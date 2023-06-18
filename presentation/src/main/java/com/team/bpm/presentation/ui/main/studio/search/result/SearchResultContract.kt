package com.team.bpm.presentation.ui.main.studio.search.result

import com.team.bpm.domain.model.Keyword
import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.base.BaseContract

interface SearchResultContract : BaseContract<SearchResultContract.State, SearchResultContract.Event, SearchResultContract.Effect> {
    data class State(
        val isLoading: Boolean = false,
        val studioList: List<Studio> = emptyList(),
        val isFiltering: Boolean = false,
        val isFiltered: Boolean = false,
        val isRegionFiltering: Boolean = true,
        val firstRegion: String = "서울",
        val secondRegion: String? = null,
        val keywordList: List<Int> = emptyList(),
        val recommendKeywordMap: Map<Keyword, Boolean> = HashMap(),
        val recommendKeywordCount: Int = 0
    )

    sealed interface Event {
        object GetSearchResult : Event

        object OnClickSearch : Event

        object OnClickRegionFilter : Event

        object OnClickKeywordFilter : Event

        object OnClickFilter : Event

        object OnClickRegionTab : Event

        object OnClickKeywordTab : Event

        data class OnClickSecondRegion(val secondRegion: String) : Event

        object GetKeywordList : Event

        data class OnClickKeywordChip(val keyword: Keyword) : Event

        data class OnClickSetFilter(
            val keywordList: List<Int>,
            val region: String
        ) : Event

        data class OnClickStudio(val studioId: Int) : Event

        data class OnClickScrap(
            val studioId: Int,
            val index: Int
        ) : Event

        object OnClickReset : Event
    }

    sealed interface Effect {
        data class ShowToast(val text: String) : Effect

        object GoToSearch : Effect

        data class GoToStudioDetail(val studioId: Int) : Effect
    }
}