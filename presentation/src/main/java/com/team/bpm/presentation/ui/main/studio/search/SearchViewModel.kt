package com.team.bpm.presentation.ui.main.studio.search

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.team.bpm.domain.usecase.search.GetRecentSearchListUseCase
import com.team.bpm.domain.usecase.search.SetRecentSearchListUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getRecentSearchListUseCase: GetRecentSearchListUseCase,
    private val setRecentSearchListUseCase: SetRecentSearchListUseCase
) : BaseViewModelV2(), SearchContract {
    private val _state = MutableStateFlow(SearchContract.State())
    override val state: StateFlow<SearchContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchContract.Effect>()
    override val effect: SharedFlow<SearchContract.Effect> = _effect.asSharedFlow()

    private val gson = Gson()

    override fun event(event: SearchContract.Event) = when (event) {
        is SearchContract.Event.GetRecentSearchList -> {
            getRecentSearchList()
        }

        is SearchContract.Event.Search -> {
            search(event.text, event.shouldBeSaved)
        }

        is SearchContract.Event.OnClickDeleteRecentSearch -> {
            onClickDeleteRecentSearch(event.index)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getRecentSearchList() {
        viewModelScope.launch {
            _state.update {
                it.copy(isRecentSearchListLoading = true)
            }

            withContext(ioDispatcher) {
                getRecentSearchListUseCase().onEach { result ->
                    withContext(mainImmediateDispatcher) {
                        result?.let { recentSearchString ->
                            _state.update {
                                it.copy(
                                    isRecentSearchListLoading = false,
                                    recentSearchList = gson.fromJson(recentSearchString, object : TypeToken<List<String>>() {}.type)
                                )
                            }
                        } ?: run {
                            _state.update {
                                it.copy(isRecentSearchListLoading = false)
                            }
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
            }
        }
    }

    private fun search(
        search: String,
        shouldBeSaved: Boolean
    ) {
        if (shouldBeSaved) {
            if (search.isNotEmpty()) {
                viewModelScope.launch(ioDispatcher) {
                    val recentSearchList = mutableListOf<String>().apply {
                        add(search)
                        addAll(state.value.recentSearchList)
                    }

                    setRecentSearchListUseCase(gson.toJson(if (recentSearchList.size >= 10) recentSearchList.subList(0, 10) else recentSearchList))
                }
            }
        }

        viewModelScope.launch {
            _effect.emit(SearchContract.Effect.GoToSearchResult(search))
            _effect.emit(SearchContract.Effect.EraseSearch)
        }
    }

    private fun onClickDeleteRecentSearch(index: Int) {
        viewModelScope.launch(ioDispatcher) {
            setRecentSearchListUseCase(
                gson.toJson(state.value.recentSearchList.toMutableList().apply { removeAt(index) })
            ).launchIn(viewModelScope + exceptionHandler)
        }
    }
}