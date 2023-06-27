package com.team.bpm.presentation.ui.main.mypage.starttab

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.mypage.GetMainTabIndexUseCase
import com.team.bpm.domain.usecase.mypage.SetMainTabIndexUseCase
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import com.team.bpm.presentation.model.MainTabType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyPageStartTabViewModel @Inject constructor(
    private val getMainTabIndexUseCase: GetMainTabIndexUseCase,
    private val setMainTabIndexUseCase: SetMainTabIndexUseCase,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), MyPageStartTabContract {

    private val _state = MutableStateFlow(MyPageStartTabContract.State())
    override val state: StateFlow<MyPageStartTabContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MyPageStartTabContract.Effect>()
    override val effect: SharedFlow<MyPageStartTabContract.Effect> = _effect.asSharedFlow()

    override fun event(event: MyPageStartTabContract.Event) {
        when (event) {
            is MyPageStartTabContract.Event.EditTab -> {
                setMainTabIndex(event.index)
            }
        }
    }

    init {
        getMainTabIndex()
    }

    private fun getMainTabIndex() {
        viewModelScope.launch(ioDispatcher) {
            getMainTabIndexUseCase().onEach { index ->
                _state.update {
                    it.copy(
                        startTabIndex = index ?: 0,
                        tabList = MainTabType.tabList.apply {
                            this.map {
                                it.isSelected = it.index == index
                            }
                        }
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun setMainTabIndex(index: Int) {
        viewModelScope.launch(ioDispatcher) {
            setMainTabIndexUseCase(index).onEach { index ->
                withContext(mainImmediateDispatcher) {
                    _state.update {
                        it.copy(
                            startTabIndex = index ?: 0,
                            tabList = MainTabType.tabList.apply {
                                this.map {
                                    it.isSelected = it.index == index
                                }
                            }
                        )
                    }
                    _effect.emit(MyPageStartTabContract.Effect.ShowToast("변경이 완료되었습니다."))
                }
            }.launchIn(viewModelScope)
        }
    }
}