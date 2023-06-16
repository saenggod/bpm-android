package com.team.bpm.presentation.ui.main.search.search_result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
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
                        with(mainImmediateDispatcher) {
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
}