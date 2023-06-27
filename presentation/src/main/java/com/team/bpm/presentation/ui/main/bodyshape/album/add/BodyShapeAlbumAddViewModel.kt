package com.team.bpm.presentation.ui.main.bodyshape.album.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.making_album.EditAlbumUseCase
import com.team.bpm.domain.usecase.making_album.GetAlbumUseCase
import com.team.bpm.domain.usecase.making_album.MakeAlbumUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class BodyShapeAlbumAddViewModel @Inject constructor(
    private val getAlbumUseCase: GetAlbumUseCase,
    private val makeAlbumUseCase: MakeAlbumUseCase,
    private val editAlbumUseCase: EditAlbumUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModelV2(), BodyShapeAlbumAddContract {
    private val _state = MutableStateFlow(BodyShapeAlbumAddContract.State())
    override val state: StateFlow<BodyShapeAlbumAddContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BodyShapeAlbumAddContract.Effect>()
    override val effect: SharedFlow<BodyShapeAlbumAddContract.Effect> = _effect.asSharedFlow()

    override fun event(event: BodyShapeAlbumAddContract.Event) = when (event) {
        is BodyShapeAlbumAddContract.Event.GetAlbum -> {
            getAlbum()
        }

        is BodyShapeAlbumAddContract.Event.OnClickEdit -> {
            onClickEdit()
        }

        is BodyShapeAlbumAddContract.Event.OnClickDate -> {
            onClickDate(event.date)
        }

        is BodyShapeAlbumAddContract.Event.OnClickSubmit -> {
            onClickSubmit(event.albumName, event.memo)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getAlbumId(): Int? {
        return savedStateHandle.get<Int>(BodyShapeAlbumAddActivity.KEY_ALBUM_ID)
    }

    private fun getAlbum() {
        getAlbumId()?.let { albumId ->
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        isLoading = true,
                        albumId = albumId
                    )
                }

                withContext(ioDispatcher) {
                    getAlbumUseCase(albumId).onEach { album ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    fetchedAlbumName = album.albumName,
                                    selectedDate = LocalDate.parse(album.date, DateTimeFormatter.ISO_LOCAL_DATE),
                                    fetchedMemo = album.memo
                                )
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        } ?: run {
            viewModelScope.launch {
                _state.update {
                    it.copy(isEditing = true)
                }
            }
        }
    }

    private fun onClickEdit() {
        viewModelScope.launch {
            _state.update {
                it.copy(isEditing = true)
            }
        }
    }

    private fun onClickDate(date: LocalDate) {
        viewModelScope.launch {
            _state.update {
                it.copy(selectedDate = date)
            }
        }
    }

    private fun onClickSubmit(
        albumName: String,
        memo: String
    ) {
        state.value.selectedDate?.let { selectedDate ->
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    if (state.value.albumId == null) {
                        makeAlbumUseCase(
                            albumName = albumName,
                            date = selectedDate.toString(),
                            memo = memo
                        )
                    } else {
                        editAlbumUseCase(
                            albumId = state.value.albumId!!,
                            albumName = albumName,
                            date = selectedDate.toString(),
                            memo = memo
                        )
                    }.onEach { album ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                albumId = album.id,
                                isEditing = false,
                                fetchedAlbumName = album.albumName,
                                selectedDate = selectedDate,
                                fetchedMemo = album.memo
                            )
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }
}
