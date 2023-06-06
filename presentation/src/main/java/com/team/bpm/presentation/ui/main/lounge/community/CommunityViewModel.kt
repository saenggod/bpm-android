package com.team.bpm.presentation.ui.main.lounge.community

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.community.GetCommunityListUseCase
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getCommunityListUseCase: GetCommunityListUseCase,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), CommunityContract {

    private val _state = MutableStateFlow(CommunityContract.State())
    override val state: StateFlow<CommunityContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CommunityContract.Effect>()
    override val effect: SharedFlow<CommunityContract.Effect> = _effect.asSharedFlow()

    // 무한스크롤을 위한 page
    var page = 0

    override fun event(event: CommunityContract.Event) {
        when (event) {
            is CommunityContract.Event.OnClickListItem -> {
                goToCommunityDetail(event.communityId)
            }
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            // TODO : Error Handling
        }
    }

    fun getCommunityList(page: Int = 0) {
        viewModelScope.launch(ioDispatcher) {
            getCommunityListUseCase(page = page, size = 20).onEach { data ->
                withContext(mainImmediateDispatcher) {
                    _state.update {
                        it.copy(
                            communityList = data.stories
                        )
                    }
                }
            }.launchIn(viewModelScope + exceptionHandler)
        }
    }

    private fun goToCommunityDetail(communityId: Int) {
        viewModelScope.launch {
            _effect.emit(CommunityContract.Effect.GoToCommunityDetail(communityId))
        }
    }
}