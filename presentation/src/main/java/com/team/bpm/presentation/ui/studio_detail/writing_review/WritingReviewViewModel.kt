package com.team.bpm.presentation.ui.studio_detail.writing_review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.usecase.splash.WriteReviewUseCase
import com.team.bpm.domain.usecase.studio_detail.StudioDetailUseCase
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WritingReviewViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val writeReviewUseCase: WriteReviewUseCase,
    private val studioDetailUseCase: StudioDetailUseCase,
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

        }
        is WritingReviewContract.Event.OnImagesAdded -> {

        }
        is WritingReviewContract.Event.OnClickRemoveImage -> {

        }
        is WritingReviewContract.Event.OnClickSubmit -> {

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

        viewModelScope.launch(ioDispatcher + exceptionHandler) {
            getStudioId()?.let { studioId ->
                studioDetailUseCase(studioId).onEach { result ->
                    withContext(mainImmediateDispatcher) {
                        when (result) {
                            is ResponseState.Success -> {
                                _state.update {
                                    it.copy(isLoading = false, studio = result.data)
                                }
                            }
                            is ResponseState.Error -> {
                                _state.update {
                                    it.copy(isLoading = false)
                                }

                                // TODO : Show error dialog
                            }
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

//    fun writeReview(
//        studioId: Int,
//        images: List<ImageBitmap>,
//        rating: Double,
//        recommends: List<String>,
//        content: String
//    ) {
//        viewModelScope.launch(mainDispatcher) {
//            _state.emit(WritingReviewState.Loading)
//        }
//
////        viewModelScope.launch(ioDispatcher + exceptionHandler) {
////            writeReviewUseCase(
////                studioId = studioId,
////                images = images.map { convertBitmapToWebpFile(it) },
////                rating = rating,
////                recommends = recommends,
////                content = content
////            ).onEach { state ->
////                when(state) {
////                    is ResponseState.Success -> _state.emit(WritingReviewState.ReviewSuccess(state.data))
////                    is ResponseState.Error -> _state.emit(WritingReviewState.Error)
////                }
////            }.launchIn(viewModelScope)
////        }
//    }
//
//    fun onClickWriteReview() {
//        viewModelScope.launch(mainDispatcher) {
//            _event.emit(WritingReviewViewEvent.Write)
//        }
//    }
}