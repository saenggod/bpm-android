package com.team.bpm.presentation.ui.main.studio.search.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Keyword
import com.team.bpm.domain.usecase.review.GetKeywordListUseCase
import com.team.bpm.domain.usecase.search.GetFilteredStudioListUseCase
import com.team.bpm.domain.usecase.search_studio.SearchStudioUseCase
import com.team.bpm.domain.usecase.studio.ScrapCancelUseCase
import com.team.bpm.domain.usecase.studio.ScrapUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val searchStudioUseCase: SearchStudioUseCase,
    private val scrapUseCase: ScrapUseCase,
    private val scrapCancelUseCase: ScrapCancelUseCase,
    private val getKeywordListUseCase: GetKeywordListUseCase,
    private val getFilteredStudioListUseCase: GetFilteredStudioListUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModelV2(), SearchResultContract {

    private val _state = MutableStateFlow(SearchResultContract.State())
    override val state: StateFlow<SearchResultContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchResultContract.Effect>()
    override val effect: SharedFlow<SearchResultContract.Effect> = _effect.asSharedFlow()

    override fun event(event: SearchResultContract.Event) = when (event) {
        is SearchResultContract.Event.GetSearchResult -> {
            getSearchResult()
        }

        is SearchResultContract.Event.OnClickSearch -> {
            onClickSearch()
        }

        is SearchResultContract.Event.OnClickRegionFilter -> {
            onClickRegionFilter()
        }

        is SearchResultContract.Event.OnClickKeywordFilter -> {
            onClickKeywordFilter()
        }

        is SearchResultContract.Event.OnClickFilter -> {
            onClickFilter()
        }

        is SearchResultContract.Event.OnClickRegionTab -> {
            onClickRegionTab()
        }

        is SearchResultContract.Event.OnClickKeywordTab -> {
            onClickKeywordTab()
        }

        is SearchResultContract.Event.OnClickSecondRegion -> {
            onClickSecondRegion(event.secondRegion)
        }

        is SearchResultContract.Event.GetKeywordList -> {
            getKeywordList()
        }

        is SearchResultContract.Event.OnClickKeywordChip -> {
            onClickKeywordChip(event.keyword)
        }

        is SearchResultContract.Event.OnClickSetFilter -> {
            onClickSetFilter(event.keywordList, event.region)
        }

        is SearchResultContract.Event.OnClickStudio -> {
            onClickStudio(event.studioId)
        }

        is SearchResultContract.Event.OnClickScrap -> {
            onClickScrap(event.studioId, event.index)
        }

        is SearchResultContract.Event.OnClickReset -> {
            onClickReset()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getSearch(): String? {
        return savedStateHandle.get<String>(SearchResultActivity.KEY_SEARCH)
    }

    private fun getSearchResult() {
        getSearch()?.let { search ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    searchStudioUseCase(search).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            result.studios?.let { studios ->
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        studioList = studios
                                    )
                                }
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }

    private fun onClickSearch() {
        viewModelScope.launch {
            _effect.emit(SearchResultContract.Effect.GoToSearch)
        }
    }

    private fun onClickRegionFilter() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isFiltering = true,
                    isRegionFiltering = true
                )
            }
        }
    }

    private fun onClickKeywordFilter() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isFiltering = true,
                    isRegionFiltering = false
                )
            }
        }
    }

    private fun onClickFilter() {
        viewModelScope.launch {
            _state.update {
                it.copy(isFiltering = true)
            }
        }
    }

    private fun onClickRegionTab() {
        viewModelScope.launch {
            _state.update {
                it.copy(isRegionFiltering = true)
            }
        }
    }

    private fun onClickKeywordTab() {
        viewModelScope.launch {
            _state.update {
                it.copy(isRegionFiltering = false)
            }
        }
    }

    private fun onClickSecondRegion(secondRegion: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(secondRegion = if (secondRegion == it.secondRegion) null else secondRegion)
            }
        }
    }

    private fun getKeywordList() {
        viewModelScope.launch(ioDispatcher) {
            getKeywordListUseCase().onEach { result ->
                val recommendKeywordMap = HashMap<Keyword, Boolean>()
                withContext(mainImmediateDispatcher) {
                    result.keywords?.forEach { keyword ->
                        recommendKeywordMap[keyword] = false
                    }

                    _state.update {
                        it.copy(recommendKeywordMap = recommendKeywordMap)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun onClickKeywordChip(keyword: Keyword) {
        viewModelScope.launch {
            with(state.value) {
                if (recommendKeywordMap[keyword] == true) {
                    _state.update {
                        it.copy(
                            recommendKeywordMap = it.recommendKeywordMap.toMutableMap().apply {
                                this[keyword] = false
                            } as HashMap<Keyword, Boolean>,
                            recommendKeywordCount = recommendKeywordCount - 1
                        )
                    }
                } else {
                    if (recommendKeywordCount == 5) {
                        _effect.emit(SearchResultContract.Effect.ShowToast("5개 이상 선택할 수 없습니다."))
                    } else {
                        _state.update {
                            it.copy(
                                recommendKeywordMap = it.recommendKeywordMap.toMutableMap().apply {
                                    this[keyword] = true
                                } as HashMap<Keyword, Boolean>,
                                recommendKeywordCount = recommendKeywordCount + 1
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onClickSetFilter(keywordList: List<Int>, region: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isFiltering = false
                )
            }

            withContext(ioDispatcher) {
                getFilteredStudioListUseCase(keywordList, region).onEach { result ->
                    withContext(mainImmediateDispatcher) {
                        result.studios?.let { studios ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    studioList = studios
                                )
                            }
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
            }
        }
    }

    private fun onClickStudio(studioId: Int) {
        viewModelScope.launch {
            _effect.emit(SearchResultContract.Effect.GoToStudioDetail(studioId))
        }
    }

    private fun onClickScrap(studioId: Int, index: Int) {
        val studio = state.value.studioList[index]
        viewModelScope.launch {
            when (studio.scrapped) {
                true -> {
                    scrapCancelUseCase(studioId).onEach {
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(studioList = it.studioList.toMutableList().apply {
                                    this[index] = this[index].copy(
                                        scrapped = false
                                    )
                                })
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }

                false -> {
                    scrapUseCase(studioId).onEach {
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(studioList = it.studioList.toMutableList().apply {
                                    this[index] = this[index].copy(
                                        scrapped = true
                                    )
                                })
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }

                null -> {
                    withContext(mainImmediateDispatcher) {
                        _effect.emit(SearchResultContract.Effect.ShowToast("스크랩 기능을 사용할 수 없습니다."))
                    }
                }
            }
        }
    }

    private fun onClickReset() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    secondRegion = null,
                    recommendKeywordMap = it.recommendKeywordMap.toMutableMap().apply {
                        forEach { keyword ->
                            this[keyword.key] = false
                        }
                    }
                )
            }
        }
    }
}