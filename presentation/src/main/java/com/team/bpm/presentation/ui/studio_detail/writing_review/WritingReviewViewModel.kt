package com.team.bpm.presentation.ui.studio_detail.writing_review

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.review.SendReviewUseCase
import com.team.bpm.domain.usecase.studio.GetStudioDetailUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import com.team.bpm.presentation.util.convertImageBitmapToByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WritingReviewViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val sendReviewUseCase: SendReviewUseCase,
    private val getStudioDetailUseCase: GetStudioDetailUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), WritingReviewContract {

    private val _state = MutableStateFlow(WritingReviewContract.State())
    override val state: StateFlow<WritingReviewContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WritingReviewContract.Effect>()
    override val effect: SharedFlow<WritingReviewContract.Effect> = _effect.asSharedFlow()
    override fun event(event: WritingReviewContract.Event) = when (event) {
        WritingReviewContract.Event.GetStudio -> {
            getStudio()
        }
        WritingReviewContract.Event.OnClickImagePlaceHolder -> {
            onClickImagePlaceHolder()
        }
        is WritingReviewContract.Event.OnImagesAdded -> {
            onImagesAdded(event.images)
        }
        is WritingReviewContract.Event.OnClickRemoveImage -> {
            onClickRemoveImage(event.index)
        }
        is WritingReviewContract.Event.OnClickKeywordChip -> {
            onClickKeywordChip(event.keyword)
        }
        is WritingReviewContract.Event.OnClickSubmit -> {
            onClickSubmit(event.rating, event.content)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getStudioId(): Int? {
        return savedStateHandle.get<Int>(WritingReviewActivity.KEY_STUDIO_ID)
    }

    private fun getStudio() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
        }

        viewModelScope.launch(ioDispatcher) {
            getStudioId()?.let { studioId ->
                getStudioDetailUseCase(studioId).onEach { result ->
                    withContext(mainImmediateDispatcher) {
                        _state.update {
                            it.copy(isLoading = false, studio = result)
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
            }
        }
    }

    private fun onClickImagePlaceHolder() {
        viewModelScope.launch {
            _effect.emit(WritingReviewContract.Effect.AddImages)
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

    private fun onClickRemoveImage(index: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(imageList = it.imageList.toMutableList().apply { removeAt(index) })
            }
        }
    }

    private fun onClickKeywordChip(keyword: String) {
        viewModelScope.launch {
            with(state.value) {
                if (recommendKeywordMap[keyword] == true) {
                    _state.update {
                        it.copy(
                            recommendKeywordMap = state.value.recommendKeywordMap.toMutableMap().apply {
                                this[keyword] = false
                            } as HashMap<String, Boolean>,
                            recommendKeywordCount = recommendKeywordCount - 1
                        )
                    }
                } else {
                    if (recommendKeywordCount == 5) {
                        _effect.emit(WritingReviewContract.Effect.ShowToast("5개 이상 선택할 수 없습니다."))
                    } else {
                        _state.update {
                            it.copy(
                                recommendKeywordMap = state.value.recommendKeywordMap.toMutableMap().apply {
                                    this[keyword] = true
                                } as HashMap<String, Boolean>,
                                recommendKeywordCount = recommendKeywordCount + 1
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onClickSubmit(rating: Double, content: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            withContext(ioDispatcher) {
                state.value.studio?.id?.let { studioId ->
                    sendReviewUseCase(
                        studioId = studioId,
                        imageByteArrays = state.value.imageList.map { convertImageBitmapToByteArray(it.second) },
                        rating = rating,
                        recommends = state.value.recommendKeywordMap.keys.toList(),
                        content = content
                    ).onEach {
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(isLoading = false)
                            }

                            _effect.emit(WritingReviewContract.Effect.ShowToast("리뷰를 작성하였습니다."))
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }
}