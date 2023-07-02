package com.team.bpm.presentation.ui.main.mypage

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Error
import com.team.bpm.domain.usecase.mypage.GetMyPageMainDataUseCase
import com.team.bpm.presentation.BuildConfig
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
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
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyPageMainDataUseCase: GetMyPageMainDataUseCase,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), MyPageContract {

    private val _state = MutableStateFlow(MyPageContract.State())
    override val state: StateFlow<MyPageContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MyPageContract.Effect>()
    override val effect: SharedFlow<MyPageContract.Effect> = _effect.asSharedFlow()

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            val error = throwable as? Error
            viewModelScope.launch {
                _effect.emit(
                    MyPageContract.Effect.ShowToast(
                        error?.message ?: "오류가 발생했습니다. 다시 시도해주세요."
                    )
                )
            }
        }
    }

    override fun event(event: MyPageContract.Event) {
        viewModelScope.launch(mainImmediateDispatcher) {
            when (event) {
                MyPageContract.Event.Notification -> {
                    _effect.emit(MyPageContract.Effect.GoNotification)
                }
                MyPageContract.Event.Profile -> {
                    _effect.emit(MyPageContract.Effect.GoProfileManage)
                }
                MyPageContract.Event.MyPost -> {
                    _effect.emit(MyPageContract.Effect.GoMyPost)
                }
                MyPageContract.Event.MyQuestion -> {
                    _effect.emit(MyPageContract.Effect.GoMyQuestion)
                }
                MyPageContract.Event.ScrappedStudios -> {
                    _effect.emit(MyPageContract.Effect.GoScrappedStudios)
                }
                MyPageContract.Event.StartTab -> {
                    _effect.emit(MyPageContract.Effect.GoEditStartTab)
                }
                MyPageContract.Event.Notice -> {
                    _effect.emit(MyPageContract.Effect.ShowToast("준비중입니다."))
                }
                MyPageContract.Event.Version -> {
                    _effect.emit(MyPageContract.Effect.ShowToast("현재 버전은 v${BuildConfig.VERSION_NAME} 입니다."))
                }
            }
        }
    }

    fun getMyPageInfo() {
        viewModelScope.launch(ioDispatcher) {
            getMyPageMainDataUseCase().onEach { data ->
                _state.update {
                    it.copy(
                        userName = data.first.nickname,
                        userDescription = data.first.bio,
                        userImage = data.first.image,
                        isNewNotification = data.second
                    )
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    fun onClickNotification() {
        event(MyPageContract.Event.Notification)
    }

    fun onClickMyPost() {
        event(MyPageContract.Event.MyPost)
    }

    fun onClickMyQuestion() {
        event(MyPageContract.Event.MyQuestion)
    }

    fun onClickScrappedStudios() {
        event(MyPageContract.Event.ScrappedStudios)
    }

    fun onClickProfile() {
        event(MyPageContract.Event.Profile)
    }

    fun onClickSetStartTab() {
        event(MyPageContract.Event.StartTab)
    }

    fun onClickNotice() {
        event(MyPageContract.Event.Notice)
    }

    fun onClickVersion() {
        event(MyPageContract.Event.Version)
    }
}