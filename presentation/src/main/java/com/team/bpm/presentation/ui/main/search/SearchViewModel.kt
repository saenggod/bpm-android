package com.team.bpm.presentation.ui.main.search

import com.team.bpm.presentation.base.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*

@HiltViewModel
class SearchViewModel : BaseViewModelV2(), SearchContract {
    private val _state = MutableStateFlow(SearchContract.State())
    override val state: StateFlow<SearchContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchContract.Effect>()
    override val effect: SharedFlow<SearchContract.Effect> = _effect.asSharedFlow()

    override fun event(event: SearchContract.Event) = when (event) {
        is SearchContract.Event.OnClickSearch -> {

        }
    }
}