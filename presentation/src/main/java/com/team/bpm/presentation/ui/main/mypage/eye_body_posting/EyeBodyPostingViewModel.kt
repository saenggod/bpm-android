package com.team.bpm.presentation.ui.main.mypage.eye_body_posting

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.presentation.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EyeBodyPostingViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), EyeBodyPostingContract {
    private val _state = MutableStateFlow(EyeBodyPostingContract.State())
    override val state: StateFlow<EyeBodyPostingContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<EyeBodyPostingContract.Effect>()
    override val effect: SharedFlow<EyeBodyPostingContract.Effect> = _effect.asSharedFlow()

    override fun event(event: EyeBodyPostingContract.Event) = when (event) {
        is EyeBodyPostingContract.Event.OnClickBackButton -> {
            onClickBackButton()
        }
        is EyeBodyPostingContract.Event.OnClickImagePlaceHolder -> {
            onClickImagePlaceHolder()
        }
        is EyeBodyPostingContract.Event.OnClickRemoveImage -> {
            onClickRemoveImage(event.index)
        }
        is EyeBodyPostingContract.Event.OnImagesAdded -> {
            onImagesAdded(event.images)
        }
    }

    private fun onClickBackButton() {
        viewModelScope.launch {
            _effect.emit(EyeBodyPostingContract.Effect.GoBack)
        }
    }

    private fun onClickImagePlaceHolder() {
        viewModelScope.launch {
            _effect.emit(EyeBodyPostingContract.Effect.AddImages)
        }
    }

    private fun onClickRemoveImage(index: Int) {
        _state.update {
            it.copy(imageList = it.imageList.toMutableList().apply { removeAt(index) })
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
}