package com.team.bpm.presentation.ui.main.bodyshape.album

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Error
import com.team.bpm.domain.usecase.body_shape.DeleteAlbumUseCase
import com.team.bpm.domain.usecase.body_shape.GetBodyShapeAlbumInfoUseCase
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
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
class BodyShapeAlbumViewModel @Inject constructor(
    private val getBodyShapeAlbumInfoUseCase: GetBodyShapeAlbumInfoUseCase,
    private val deleteAlbumUseCase: DeleteAlbumUseCase,
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

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->
            val error = throwable as? Error
            viewModelScope.launch {
                _effect.emit(
                    BodyShapeAlbumContract.Effect.ShowToast(
                        error?.message ?: "오류가 발생했습니다. 다시 시도해주세요."
                    )
                )
            }
        }
    }

    override fun event(event: BodyShapeAlbumContract.Event) {
        when (event) {
            is BodyShapeAlbumContract.Event.OnClickBodyShapeDetail -> {
                goToBodyShapeDetail(event.albumDetailId)
            }
            BodyShapeAlbumContract.Event.OnClickMore -> {
                showMoreBottomSheet()
            }
            BodyShapeAlbumContract.Event.OnClickEditAlbumDetail -> {
                goToEditAlbumDetailPosting()
            }
            BodyShapeAlbumContract.Event.OnClickAddBodyShapeDetailPosting -> {
                goToAddBodyShapeDetailPosting()
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
                                albumInfo = data,
                                isPostToday = data.isTodayPost
                            )
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }

    fun deleteAlbum() {
        viewModelScope.launch(ioDispatcher) {
            albumId?.let { id ->
                deleteAlbumUseCase(id)
                    .onEach { data ->
                        _effect.emit(BodyShapeAlbumContract.Effect.ShowToast("삭제가 완료되었습니다."))
                        _effect.emit(BodyShapeAlbumContract.Effect.GoOutThisPage)
                    }.launchIn(viewModelScope + exceptionHandler)
            }
        }
    }

    fun onClickAddPost() {
        event(BodyShapeAlbumContract.Event.OnClickAddBodyShapeDetailPosting)
    }

    fun onClickAlbumDetail(albumDetailId: Int) {
        event(BodyShapeAlbumContract.Event.OnClickBodyShapeDetail(albumDetailId))
    }

    fun onClickAlbumEdit() {
        event(BodyShapeAlbumContract.Event.OnClickEditAlbumDetail)
    }

    fun onClickMore() {
        event(BodyShapeAlbumContract.Event.OnClickMore)
    }

    private fun goToEditAlbumDetailPosting() {
        viewModelScope.launch {
            state.value.albumInfo?.id?.let { albumId ->
                _effect.emit(
                    BodyShapeAlbumContract.Effect.GoToEditAlbumDetail(albumId)
                )
            }
        }
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

    private fun goToBodyShapeDetail(albumDetailId: Int) {

        val dday = state.value.albumInfo?.bodyShapeList?.bodyShapeDetails
            ?.firstOrNull { it.id == albumDetailId }?.dday ?: 0

        viewModelScope.launch {
            state.value.albumInfo?.id?.let { albumId ->
                _effect.emit(
                    BodyShapeAlbumContract.Effect.GoToBodyShapeDetail(
                        albumId,
                        albumDetailId,
                        dday
                    )
                )
            }
        }
    }

    private fun showMoreBottomSheet() {
        viewModelScope.launch {
            _effect.emit(BodyShapeAlbumContract.Effect.ShowMoreBottomSheet)
        }
    }
}