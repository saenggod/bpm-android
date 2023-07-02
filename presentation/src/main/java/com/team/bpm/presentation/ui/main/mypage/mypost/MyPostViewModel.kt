package com.team.bpm.presentation.ui.main.mypage.mypost

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Community
import com.team.bpm.domain.model.Error
import com.team.bpm.domain.usecase.question.DeleteQuestionUseCase
import com.team.bpm.domain.usecase.question.GetMyPostListUseCase
import com.team.bpm.domain.usecase.question.GetMyQuestionListUseCase
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
class MyPostViewModel @Inject constructor(
    private val getMyPostListUseCase: GetMyPostListUseCase,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), MyPostContract {

    private val _state = MutableStateFlow(MyPostContract.State())
    override val state: StateFlow<MyPostContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MyPostContract.Effect>()
    override val effect: SharedFlow<MyPostContract.Effect> = _effect.asSharedFlow()

    var offset: Int = 0

    override fun event(event: MyPostContract.Event) {
        when (event) {
            is MyPostContract.Event.OnClickListItem -> {
                goToCommunityDetail(event.communityId)
            }
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            val error = throwable as? Error
            viewModelScope.launch {
                _effect.emit(
                    MyPostContract.Effect.ShowToast(
                        error?.message ?: "오류가 발생했습니다. 다시 시도해주세요."
                    )
                )
            }
        }
    }

    // TODO : 무한스크롤 (차기 버전에서 적용)
    fun getCommunityList(page: Int = 0) {
        viewModelScope.launch(ioDispatcher) {
            getMyPostListUseCase(page = page, size = 200).onEach { data ->
                val dataList = arrayListOf<Community>()
                _state.update {
                    dataList.addAll(data.communities ?: emptyList())
                    dataList.addAll(it.communityList ?: emptyList())
                    it.copy(
                        communityList = dataList.distinctBy { it.id }
                    )
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    private fun goToCommunityDetail(communityId: Int) {
        viewModelScope.launch {
            _effect.emit(MyPostContract.Effect.GoToCommunityDetail(communityId))
        }
    }
}