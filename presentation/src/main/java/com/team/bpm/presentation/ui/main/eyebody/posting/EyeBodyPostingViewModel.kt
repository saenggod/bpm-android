package com.team.bpm.presentation.ui.main.eyebody.posting

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.eye_body.WriteEyeBodyUseCase
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
class EyeBodyPostingViewModel @Inject constructor(private val writeEyeBodyUseCase: WriteEyeBodyUseCase) : BaseViewModelV2(),
    EyeBodyPostingContract {
    private val _state = MutableStateFlow(EyeBodyPostingContract.State())
    override val state: StateFlow<EyeBodyPostingContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<EyeBodyPostingContract.Effect>()
    override val effect: SharedFlow<EyeBodyPostingContract.Effect> = _effect.asSharedFlow()

    override fun event(event: EyeBodyPostingContract.Event) = when (event) {
        is EyeBodyPostingContract.Event.OnClickImagePlaceHolder -> {
            onClickImagePlaceHolder()
        }

        is EyeBodyPostingContract.Event.OnClickRemoveImage -> {
            onClickRemoveImage(event.index)
        }

        is EyeBodyPostingContract.Event.OnImagesAdded -> {
            onImagesAdded(event.images)
        }

        is EyeBodyPostingContract.Event.OnClickSubmit -> {
            onClickSubmit(event.content)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun onClickImagePlaceHolder() {
        viewModelScope.launch {
            _effect.emit(EyeBodyPostingContract.Effect.AddImages)
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
        viewModelScope.launch {
            if (content.isNotEmpty()) {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    writeEyeBodyUseCase(content, state.value.imageList.map { image -> convertImageBitmapToByteArray(image.second) }).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            result.id?.let { eyeBodyId -> _effect.emit(
                                EyeBodyPostingContract.Effect.RedirectToEyeBody(
                                    eyeBodyId
                                )
                            ) }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            } else {
                _effect.emit(EyeBodyPostingContract.Effect.ShowToast("내용을 입력해주세요."))
            }
        }
    }
}