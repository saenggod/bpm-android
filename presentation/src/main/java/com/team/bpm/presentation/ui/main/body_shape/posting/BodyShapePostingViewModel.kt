package com.team.bpm.presentation.ui.main.body_shape.posting

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.body_shape.WriteBodyShapeUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import com.team.bpm.presentation.util.convertImageBitmapToByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BodyShapePostingViewModel @Inject constructor(
    private val writeBodyShapeUseCase: WriteBodyShapeUseCase,
    private val savedStateHandle: SavedStateHandle
    ) : BaseViewModelV2(),
    BodyShapePostingContract {
    private val _state = MutableStateFlow(BodyShapePostingContract.State())
    override val state: StateFlow<BodyShapePostingContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BodyShapePostingContract.Effect>()
    override val effect: SharedFlow<BodyShapePostingContract.Effect> = _effect.asSharedFlow()

    override fun event(event: BodyShapePostingContract.Event) = when (event) {
        is BodyShapePostingContract.Event.OnClickImagePlaceHolder -> {
            onClickImagePlaceHolder()
        }

        is BodyShapePostingContract.Event.OnClickRemoveImage -> {
            onClickRemoveImage(event.index)
        }

        is BodyShapePostingContract.Event.OnImagesAdded -> {
            onImagesAdded(event.images)
        }

        is BodyShapePostingContract.Event.OnClickSubmit -> {
            onClickSubmit(event.content)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getAlbumId(): Int? {
        return savedStateHandle.get<Int>(BodyShapePostingActivity.KEY_ALBUM_ID)
    }

    private fun onClickImagePlaceHolder() {
        viewModelScope.launch {
            _effect.emit(BodyShapePostingContract.Effect.AddImages)
        }
    }

    private fun onClickRemoveImage(index: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(imageList = it.imageList.toMutableList().apply { removeAt(index) })
            }
        }
    }

    private fun onImagesAdded(images: List<Pair<Uri, ImageBitmap>>) {
        val linkedList = LinkedList<Pair<Uri, ImageBitmap>>().apply {
            addAll(state.value.imageList)
        }

        for (i in images.indices) {
            if (linkedList.size == 5) {
                break
            }

            linkedList.addFirst(images[i])
        }

        _state.update {
            it.copy(imageList = linkedList.toMutableList())
        }
    }

    private fun onClickSubmit(content: String) {
        getAlbumId()?.let { albumId ->
            viewModelScope.launch {
                if (content.isNotEmpty()) {
                    _state.update {
                        it.copy(isLoading = true)
                    }

                    withContext(ioDispatcher) {
                        writeBodyShapeUseCase(albumId, content, state.value.imageList.map { image -> convertImageBitmapToByteArray(image.second) }).onEach { result ->
                            withContext(mainImmediateDispatcher) {
                                result.id?.let { bodyShapeId -> _effect.emit(
                                    BodyShapePostingContract.Effect.RedirectToBodyShape(
                                        bodyShapeId
                                    )
                                ) }
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }
                } else {
                    _effect.emit(BodyShapePostingContract.Effect.ShowToast("내용을 입력해주세요."))
                }
            }
        }
    }
}