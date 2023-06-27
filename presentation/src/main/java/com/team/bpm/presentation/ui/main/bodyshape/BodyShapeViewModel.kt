package com.team.bpm.presentation.ui.main.bodyshape

import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.body_shape.GetBodyShapeSchedulesUseCase
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
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
import javax.inject.Inject

@HiltViewModel
class BodyShapeViewModel @Inject constructor(
    private val getBodyShapeSchedulesUseCase: GetBodyShapeSchedulesUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), BodyShapeContract {

    private val _state = MutableStateFlow(BodyShapeContract.State())
    override val state: StateFlow<BodyShapeContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BodyShapeContract.Effect>()
    override val effect: SharedFlow<BodyShapeContract.Effect> = _effect.asSharedFlow()

    override fun event(event: BodyShapeContract.Event) {
        viewModelScope.launch {
            when (event) {
                BodyShapeContract.Event.OnClickCreateBodyShape -> {
                    _effect.emit(BodyShapeContract.Effect.GoCreateBodyShape)
                }
                is BodyShapeContract.Event.OnClickBodyShapeDetail -> {
                    _effect.emit(BodyShapeContract.Effect.GoBodyShapeDetail(event.id))
                }
                is BodyShapeContract.Event.OnClickBodyShapePosting -> {
                    _effect.emit(BodyShapeContract.Effect.GoBodyShapePosting(event.id))
                }
            }
        }
    }

    fun getUserSchedule() {
        viewModelScope.launch(ioDispatcher) {
            getBodyShapeSchedulesUseCase().onEach { data ->
                _state.update {
                    it.copy(
                        bodyShapeInfo = data,
                        isEmpty = data.scheduleCount == 0
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onClickBodyShapeDetail(id: Int) {
        event(BodyShapeContract.Event.OnClickBodyShapeDetail(id))
    }

    fun onClickBodyShapePosting(id: Int) {
        event(BodyShapeContract.Event.OnClickBodyShapePosting(id))
    }

    fun onClickCreateBodyShape() {
        event(BodyShapeContract.Event.OnClickCreateBodyShape)
    }
}