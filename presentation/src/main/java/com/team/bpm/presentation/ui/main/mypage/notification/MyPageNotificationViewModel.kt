package com.team.bpm.presentation.ui.main.mypage.notification

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.team.bpm.domain.model.Error
import com.team.bpm.domain.model.Notification
import com.team.bpm.domain.usecase.user.GetNotificationUseCase
import com.team.bpm.domain.usecase.user.SetNotificationIsReadUseCase
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class MyPageNotificationViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
    private val setNotificationIsReadUseCase: SetNotificationIsReadUseCase,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), MyPageNotificationContract {

    private val _state = MutableStateFlow(MyPageNotificationContract.State())
    override val state: StateFlow<MyPageNotificationContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MyPageNotificationContract.Effect>()
    override val effect: SharedFlow<MyPageNotificationContract.Effect> = _effect.asSharedFlow()

    private val _notificationList = MutableStateFlow<PagingData<Notification>>(PagingData.empty())
    val notificationList: StateFlow<PagingData<Notification>> = _notificationList.asStateFlow()

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            val error = throwable as? Error
            viewModelScope.launch {
                _effect.emit(
                    MyPageNotificationContract.Effect.ShowToast(
                        error?.message ?: "오류가 발생했습니다. 다시 시도해주세요."
                    )
                )
            }
        }
    }

    override fun event(event: MyPageNotificationContract.Event) {
        when (event) {
            is MyPageNotificationContract.Event.ClickNotification -> {
                if (!event.item.read) {
                    setNotificationIsRead(event.item.id)
                }
                goToNotificationDetail(event.item)
            }
        }
    }

    fun getNotifications() {
        viewModelScope.launch(ioDispatcher) {
            getNotificationUseCase()
                .cachedIn(viewModelScope)
                .catch {
                    _effect.emit(
                        MyPageNotificationContract.Effect.ShowToast(
                            it.message ?: "오류가 발생했습니다"
                        )
                    )
                }
                .collect {
                    _notificationList.value = it
                }
        }
    }

    private fun setNotificationIsRead(notificationId: Long) {
        viewModelScope.launch {
            setNotificationIsReadUseCase(notificationId)
                .onEach { }
                .launchIn(viewModelScope + exceptionHandler)
        }
    }

    private fun goToNotificationDetail(item: Notification) {
        viewModelScope.launch {
            _effect.emit(
                MyPageNotificationContract.Effect.GoToNotificationDetail(item)
            )
        }
    }
}