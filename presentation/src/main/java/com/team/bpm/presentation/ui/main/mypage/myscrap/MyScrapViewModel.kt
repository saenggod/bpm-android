package com.team.bpm.presentation.ui.main.mypage.myscrap

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.studio.GetMyScrapListUseCase
import com.team.bpm.domain.usecase.studio.ScrapCancelUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyScrapViewModel @Inject constructor(
    private val getMyScrapListUseCase: GetMyScrapListUseCase,
    private val scrapCancelUseCase: ScrapCancelUseCase
) : BaseViewModelV2(), MyScrapContract {

    private val _state = MutableStateFlow(MyScrapContract.State())
    override val state: StateFlow<MyScrapContract.State> = _state

    private val _effect = MutableSharedFlow<MyScrapContract.Effect>()
    override val effect: SharedFlow<MyScrapContract.Effect> = _effect

    override fun event(event: MyScrapContract.Event) = when (event) {
        is MyScrapContract.Event.GetMyScrapList -> {
            getMyScrapList()
        }

        is MyScrapContract.Event.OnClickStudio -> {
            onClickStudio(event.studioId)
        }

        is MyScrapContract.Event.OnClickCancelScrap -> {
            onClickCancelScrap(event.studioId)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getMyScrapList() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            withContext(ioDispatcher) {
                getMyScrapListUseCase().onEach { result ->
                    withContext(mainImmediateDispatcher) {
                        result.studios?.let { myScrapList ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    myScrapList = myScrapList,
                                    scrapCount = result.studioCount ?: myScrapList.size
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
            _effect.emit(MyScrapContract.Effect.GoToStudioDetail(studioId))
        }
    }

    private fun onClickCancelScrap(studioId: Int) {
        viewModelScope.launch(ioDispatcher) {
            scrapCancelUseCase(studioId).onEach {
                withContext(mainImmediateDispatcher) {
                    _effect.emit(MyScrapContract.Effect.RefreshMyScrapList)
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }
}