package com.team.bpm.presentation.ui.main.studio.recommend

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Error
import com.team.bpm.domain.model.Studio
import com.team.bpm.domain.usecase.main.GetStudioListUseCase
import com.team.bpm.domain.usecase.studio.ScrapCancelUseCase
import com.team.bpm.domain.usecase.studio.ScrapUseCase
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.model.StudioMainTabType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class StudioHomeRecommendViewModel @Inject constructor(
    private val getStudioListUseCase: GetStudioListUseCase,
    private val scrapUseCase: ScrapUseCase,
    private val scrapCancelUseCase: ScrapCancelUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _state = MutableStateFlow<StudioHomeRecommendState>(StudioHomeRecommendState.Init)
    val state: StateFlow<StudioHomeRecommendState>
        get() = _state

    private val _event = MutableSharedFlow<StudioHomeRecommendViewEvent>()
    val event: SharedFlow<StudioHomeRecommendViewEvent>
        get() = _event

    private val _list = MutableStateFlow<List<Studio>>(emptyList())
    val list: StateFlow<List<Studio>>
        get() = _list

    val type: StudioMainTabType
        get() = savedStateHandle.get<StudioMainTabType>(StudioHomeRecommendFragment.KEY_TYPE)
            ?: StudioMainTabType.POPULAR


    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            val error = throwable as? Error
            viewModelScope.launch {
                error?.let { _state.emit(StudioHomeRecommendState.Error(error)) }
            }
        }
    }

    fun getStudioList() {
        viewModelScope.launch(ioDispatcher) {
            getStudioListUseCase(
                limit = LIST_MAX,
                offset = 0,
                type = type.name.lowercase()
            ).onEach { state ->
                state.studios?.let { _list.emit(it) }
                _list.emit(state.studios ?: emptyList())
                _state.emit(StudioHomeRecommendState.List)
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    fun clickStudioDetail(studioId: Int?) {
        viewModelScope.launch {
            _event.emit(StudioHomeRecommendViewEvent.ClickDetail(studioId))
        }
    }

    fun clickStudioScrap(studioId: Int?, isScrapped: Boolean?) {
        studioId?.let { id ->
            viewModelScope.launch {
                when (isScrapped) {
                    true -> {
                        scrapCancelUseCase(id)
                            .onEach {
//                                _state.emit(StudioHomeRecommendState.Init)
                            }.launchIn(viewModelScope + exceptionHandler)
                    }
                    false -> {
                        scrapUseCase(id)
                            .onEach {
//                                _state.emit(StudioHomeRecommendState.Init)
                            }.launchIn(viewModelScope + exceptionHandler)
                    }
                    else -> Unit
                }
            }
        }
    }

    companion object {
        // 첫 페이지에서의 무한정 데이터는 앱을 느리게해요..
        const val LIST_MAX = 5

    }
}