package com.team.bpm.presentation.ui.main.search

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
            search(event.text)
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

    private fun search(search: String) {
        if (search.isNotEmpty()) {
            viewModelScope.launch(ioDispatcher) {
                setRecentSearchListUseCase(
                    gson.toJson(
                        mutableListOf<String>().apply {
                            add(search)
                            addAll(state.value.recentSearchList)
                        })
                ).onEach { result ->
                    withContext(mainImmediateDispatcher) {
                        result.let { recentSearchString ->
                            _state.update {
                                it.copy(recentSearchList = gson.fromJson(recentSearchString, object : TypeToken<List<String>>() {}.type))
                            }

                            _effect.emit(SearchContract.Effect.GoToSearchResult(search))
                            _effect.emit(SearchContract.Effect.EraseSearch)
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
            }
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