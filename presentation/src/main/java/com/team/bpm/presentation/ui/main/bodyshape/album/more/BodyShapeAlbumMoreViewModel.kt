package com.team.bpm.presentation.ui.main.bodyshape.album.more

import androidx.lifecycle.viewModelScope
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BodyShapeAlbumMoreViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
) : BaseViewModel(), BodyShapeAlbumMoreContract {

    private val _state = MutableStateFlow(BodyShapeAlbumMoreContract.State())
    override val state: StateFlow<BodyShapeAlbumMoreContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BodyShapeAlbumMoreContract.Effect>()
    override val effect: SharedFlow<BodyShapeAlbumMoreContract.Effect> = _effect.asSharedFlow()

    override fun event(event: BodyShapeAlbumMoreContract.Event) {
        viewModelScope.launch(mainImmediateDispatcher) {
            when (event) {
                BodyShapeAlbumMoreContract.Event.Edit -> {
                    _effect.emit(BodyShapeAlbumMoreContract.Effect.GoEdit)
                }
                BodyShapeAlbumMoreContract.Event.Delete -> {
                    _effect.emit(BodyShapeAlbumMoreContract.Effect.GoDelete)
                }
            }
        }
    }

    fun clickEdit() {
        event(BodyShapeAlbumMoreContract.Event.Edit)
    }

    fun clickDelete() {
        event(BodyShapeAlbumMoreContract.Event.Delete)
    }
}