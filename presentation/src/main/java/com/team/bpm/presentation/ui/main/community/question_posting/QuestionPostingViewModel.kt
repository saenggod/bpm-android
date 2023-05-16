package com.team.bpm.presentation.ui.main.community.question_posting

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
class QuestionPostingViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), QuestionPostingContract {
    private val _state = MutableStateFlow(QuestionPostingContract.State())
    override val state: StateFlow<QuestionPostingContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<QuestionPostingContract.Effect>()
    override val effect: SharedFlow<QuestionPostingContract.Effect> = _effect.asSharedFlow()

    override fun event(event: QuestionPostingContract.Event) = when (event) {
        is QuestionPostingContract.Event.OnClickBackButton -> {
            onClickBackButton()
        }
        is QuestionPostingContract.Event.OnClickImagePlaceHolder -> {
            onClickImagePlaceHolder()
        }
        is QuestionPostingContract.Event.OnClickRemoveImage -> {
            onClickRemoveImage(event.index)
        }
        is QuestionPostingContract.Event.OnImagesAdded -> {
            onImagesAdded(event.images)
        }
    }

    private fun onClickBackButton() {
        viewModelScope.launch {
            _effect.emit(QuestionPostingContract.Effect.GoBack)
        }
    }

    private fun onClickImagePlaceHolder() {
        viewModelScope.launch {
            _effect.emit(QuestionPostingContract.Effect.AddImages)
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