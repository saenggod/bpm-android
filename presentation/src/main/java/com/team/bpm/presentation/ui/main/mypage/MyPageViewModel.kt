package com.team.bpm.presentation.ui.main.mypage

import androidx.lifecycle.viewModelScope
import com.team.bpm.presentation.BuildConfig
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MyPageViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), MyPageContract {

    private val _state = MutableStateFlow(MyPageContract.State())
    override val state: StateFlow<MyPageContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MyPageContract.Effect>()
    override val effect: SharedFlow<MyPageContract.Effect> = _effect.asSharedFlow()

    override fun event(event: MyPageContract.Event) {
        viewModelScope.launch(mainImmediateDispatcher) {
            when (event) {
                MyPageContract.Event.UserData -> {
                    // TODO : 데이터 작업
                }
                MyPageContract.Event.Notification ->{
                    _effect.emit(MyPageContract.Effect.ShowToast("준비중입니다."))
                }

                MyPageContract.Event.Profile -> {
                    _effect.emit(MyPageContract.Effect.GoProfileManage)
                }

                MyPageContract.Event.PostHistory -> {
                    _effect.emit(MyPageContract.Effect.GoHistoryPost)
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

    fun onClickNotification() {
        event(MyPageContract.Event.Notification)
    }

    fun onClickProfile(){
        event(MyPageContract.Event.Profile)
    }

    fun onClickPointHistory(){
        event(MyPageContract.Event.PostHistory)
    }

    fun onClickSetStartTab(){
        event(MyPageContract.Event.StartTab)
    }

    fun onClickNotice(){
        event(MyPageContract.Event.Notice)
    }

    fun onClickVersion(){
        event(MyPageContract.Event.Version)
    }




}