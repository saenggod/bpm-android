package com.team.bpm.presentation.ui.main.studio.detail.writing_review

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.Keyword
import com.team.bpm.domain.usecase.review.GetKeywordListUseCase
import com.team.bpm.domain.usecase.review.WriteReviewUseCase
import com.team.bpm.domain.usecase.studio.GetStudioDetailUseCase
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
class WritingReviewViewModel @Inject constructor(
    private val writeReviewUseCase: WriteReviewUseCase,
    private val getStudioDetailUseCase: GetStudioDetailUseCase,
    private val getKeywordListUseCase: GetKeywordListUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModelV2(), WritingReviewContract {

    private val _state = MutableStateFlow(WritingReviewContract.State())
    override val state: StateFlow<WritingReviewContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WritingReviewContract.Effect>()
    override val effect: SharedFlow<WritingReviewContract.Effect> = _effect.asSharedFlow()

    override fun event(event: WritingReviewContract.Event) = when (event) {
        is WritingReviewContract.Event.GetStudio -> {
            getStudio()
        }

        is WritingReviewContract.Event.GetKeywordList -> {
            getKeywordList()
        }

        is WritingReviewContract.Event.OnClickImagePlaceHolder -> {
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
                            it.copy(
                                isLoading = false,
                                studio = result
                            )
                        }
                    }
                }.launchIn(viewModelScope + exceptionHandler)
            }
        }
    }

    private fun getKeywordList() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            withContext(ioDispatcher) {
                getKeywordListUseCase().onEach { result ->
                    val recommendKeywordMap = HashMap<Keyword, Boolean>()
                    withContext(mainImmediateDispatcher) {
                        result.keywords?.forEach { keyword ->
                            recommendKeywordMap[keyword] = false
                        }

                        _state.update {
                            it.copy(
                                isLoading = false,
                                recommendKeywordMap = recommendKeywordMap
                            )
                        }
                    }
                }.launchIn(viewModelScope)
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

    private fun onClickKeywordChip(keyword: Keyword) {
        viewModelScope.launch {
            with(state.value) {
                if (recommendKeywordMap[keyword] == true) {
                    _state.update {
                        it.copy(
                            recommendKeywordMap = it.recommendKeywordMap.toMutableMap().apply {
                                this[keyword] = false
                            } as HashMap<Keyword, Boolean>,
                            recommendKeywordCount = recommendKeywordCount - 1
                        )
                    }
                } else {
                    if (recommendKeywordCount == 5) {
                        _effect.emit(WritingReviewContract.Effect.ShowToast("5개 이상 선택할 수 없습니다."))
                    } else {
                        _state.update {
                            it.copy(
                                recommendKeywordMap = it.recommendKeywordMap.toMutableMap().apply {
                                    this[keyword] = true
                                } as HashMap<Keyword, Boolean>,
                                recommendKeywordCount = recommendKeywordCount + 1
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onClickSubmit(
        rating: Double,
        content: String
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            withContext(ioDispatcher) {
                state.value.studio?.id?.let { studioId ->
                    writeReviewUseCase(
                        studioId = studioId,
                        imageByteArrays = state.value.imageList.map { convertImageBitmapToByteArray(it.second) },
                        rating = rating,
                        recommends = state.value.recommendKeywordMap.filter { it.value }.keys.map { it.id ?: 0 },
                        content = content
                    ).onEach { result ->
                        withContext(mainImmediateDispatcher) {
                            _state.update {
                                it.copy(isLoading = false)
                            }

                            result.studio?.id?.let { studioId ->
                                result.id?.let { reviewId ->
                                    _effect.emit(
                                        WritingReviewContract.Effect.GoToReviewDetail(
                                            studioId,
                                            reviewId
                                        )
                                    )
                                }
                            }
                        }
                    }.launchIn(viewModelScope + exceptionHandler)
                }
            }
        }
    }
}