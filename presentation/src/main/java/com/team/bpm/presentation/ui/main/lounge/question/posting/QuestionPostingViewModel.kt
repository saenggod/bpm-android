package com.team.bpm.presentation.ui.main.lounge.question.posting

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.question.WriteQuestionUseCase
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
class QuestionPostingViewModel @Inject constructor(private val writeQuestionUseCase: WriteQuestionUseCase) : BaseViewModelV2(), QuestionPostingContract {
    private val _state = MutableStateFlow(QuestionPostingContract.State())
    override val state: StateFlow<QuestionPostingContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<QuestionPostingContract.Effect>()
    override val effect: SharedFlow<QuestionPostingContract.Effect> = _effect.asSharedFlow()

    override fun event(event: QuestionPostingContract.Event) = when (event) {
        is QuestionPostingContract.Event.OnClickImagePlaceHolder -> {
            onClickImagePlaceHolder()
        }

        is QuestionPostingContract.Event.OnClickRemoveImage -> {
            onClickRemoveImage(event.index)
        }

        is QuestionPostingContract.Event.OnImagesAdded -> {
            onImagesAdded(event.images)
        }

        is QuestionPostingContract.Event.OnClickSubmit -> {
            onClickSubmit(event.title, event.content)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun onClickImagePlaceHolder() {
        viewModelScope.launch {
            _effect.emit(QuestionPostingContract.Effect.AddImages)
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

        viewModelScope.launch {
            _state.update {
                it.copy(imageList = linkedList.toMutableList())
            }
        }
    }

    private fun onClickSubmit(title: String, content: String) {
        viewModelScope.launch {
            if (title.isNotEmpty() && content.isNotEmpty()) {
                _state.update {
                    it.copy(isLoading = true)
                }

                withContext(ioDispatcher) {
                    writeQuestionUseCase(title, content, state.value.imageList.map { image -> convertImageBitmapToByteArray(image.second) }).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            result.id?.let { questionId -> _effect.emit(
                                QuestionPostingContract.Effect.RedirectToQuestion(
                                    questionId
                                )
                            ) }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            } else {
                _effect.emit(QuestionPostingContract.Effect.ShowToast("제목과 내용을 모두 입력해주세요."))
            }
        }
    }
}