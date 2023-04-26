package com.team.bpm.presentation.ui.main.community.community_posting

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
class CommunityPostingViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), CommunityPostingContract {
    private val _state = MutableStateFlow(CommunityPostingContract.State())
    override val state: StateFlow<CommunityPostingContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CommunityPostingContract.Effect>()
    override val effect: SharedFlow<CommunityPostingContract.Effect> = _effect.asSharedFlow()

    override fun event(event: CommunityPostingContract.Event) = when (event) {
        is CommunityPostingContract.Event.OnClickBackButton -> {
            onClickBackButton()
        }
        is CommunityPostingContract.Event.OnClickImagePlaceHolder -> {
            onClickImagePlaceHolder()
        }
        is CommunityPostingContract.Event.OnClickRemoveImage -> {
            onClickRemoveImage(event.index)
        }
        is CommunityPostingContract.Event.OnImagesAdded -> {
            onImagesAdded(event.images)
        }
    }

    private fun onClickBackButton() {
        viewModelScope.launch {
            _effect.emit(CommunityPostingContract.Effect.GoBack)
        }
    }

    private fun onClickImagePlaceHolder() {
        viewModelScope.launch {
            _effect.emit(CommunityPostingContract.Effect.AddImages)
        }
    }

    private fun onClickRemoveImage(index: Int) {
        _state.update {
            it.copy(imageList = it.imageList.toMutableList().apply { removeAt(index) })
        }
    }

    private fun onImagesAdded(images: List<Pair<Uri, ImageBitmap>>) {
        val linkedList = LinkedList<Pair<Uri, ImageBitmap>>().apply {
            addAll(_state.value.imageList)
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