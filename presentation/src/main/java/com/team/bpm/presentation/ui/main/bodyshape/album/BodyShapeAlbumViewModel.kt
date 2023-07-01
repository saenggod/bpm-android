package com.team.bpm.presentation.ui.main.bodyshape.album

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.body_shape.GetBodyShapeAlbumInfoUseCase
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
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
class BodyShapeAlbumViewModel @Inject constructor(
    private val getBodyShapeAlbumInfoUseCase: GetBodyShapeAlbumInfoUseCase,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), BodyShapeAlbumContract {

    private val bundle: Bundle? by lazy {
        savedStateHandle.get<Bundle>(BodyShapeAlbumActivity.KEY_BUNDLE)
    }

    private val albumId: Int? by lazy {
        bundle?.getInt(BodyShapeAlbumActivity.KEY_ALBUM_ID)
    }

    private val _state = MutableStateFlow(BodyShapeAlbumContract.State())
    override val state: StateFlow<BodyShapeAlbumContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BodyShapeAlbumContract.Effect>()
    override val effect: SharedFlow<BodyShapeAlbumContract.Effect> = _effect.asSharedFlow()

    override fun event(event: BodyShapeAlbumContract.Event) {
        viewModelScope.launch(mainImmediateDispatcher) {
            when (event) {
                is BodyShapeAlbumContract.Event.OnClickBodyShapeDetail -> {
                    goTodBodyShapeDetail(event.albumDetailId)
                }
                BodyShapeAlbumContract.Event.OnClickAddBodyShapeDetailPosting -> {
                    goToAddBodyShapeDetailPosting()
                }
            }
        }
    }

    fun getBodyShapeAlbumInfo() {
        viewModelScope.launch(ioDispatcher) {
            albumId?.let { id ->
                getBodyShapeAlbumInfoUseCase(id)
                    .onEach { data ->
                        _state.update {
                            it.copy(
                                albumInfo = data
                            )
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }

    fun onClickAddPost() {
        event(BodyShapeAlbumContract.Event.OnClickAddBodyShapeDetailPosting)
    }


    fun onClickAlbumDetail(albumDetailId: Int) {
        event(BodyShapeAlbumContract.Event.OnClickBodyShapeDetail(albumDetailId))
    }

    private fun goToAddBodyShapeDetailPosting() {
        viewModelScope.launch {
            state.value.albumInfo?.id?.let { albumId ->
                _effect.emit(
                    BodyShapeAlbumContract.Effect.GoToAddBodyShapeDetail(albumId)
                )
            }
        }
    }

    private fun goTodBodyShapeDetail(albumDetailId: Int) {
        viewModelScope.launch {
            state.value.albumInfo?.id?.let { albumId ->
                _effect.emit(
                    BodyShapeAlbumContract.Effect.GoToBodyShapeDetail(
                        albumId,
                        albumDetailId
                    )
                )
            }
        }
    }

}