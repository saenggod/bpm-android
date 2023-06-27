package com.team.bpm.presentation.ui.main.bodyshape.detail

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.body_shape.DeleteBodyShapeUseCase
import com.team.bpm.domain.usecase.body_shape.GetBodyShapeUseCase
import com.team.bpm.presentation.base.BaseViewModelV2
import com.team.bpm.presentation.model.BottomSheetButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BodyShapeDetailViewModel @Inject constructor(
    private val getBodyShapeDetailUseCase: GetBodyShapeUseCase,
    private val deleteBodyShapeUseCase: DeleteBodyShapeUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModelV2(), BodyShapeDetailContract {

    private val _state = MutableStateFlow(BodyShapeDetailContract.State())
    override val state: StateFlow<BodyShapeDetailContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BodyShapeDetailContract.Effect>()
    override val effect: SharedFlow<BodyShapeDetailContract.Effect> = _effect.asSharedFlow()

    override fun event(event: BodyShapeDetailContract.Event) = when (event) {
        is BodyShapeDetailContract.Event.OnClickBodyShapeActionButton -> {
            onClickBodyShapeActionButton()
        }

        is BodyShapeDetailContract.Event.OnClickEditBodyShape -> {
            onClickEditBodyShape()
        }

        is BodyShapeDetailContract.Event.OnClickDeleteBodyShape -> {
            onClickDeleteBodyShape()
        }

        is BodyShapeDetailContract.Event.GetBodyShapeDetail -> {
            getBodyShapeDetail()
        }

        is BodyShapeDetailContract.Event.OnClickDismissNoticeDialog -> {
            onClickDismissNoticeDialog()
        }

        BodyShapeDetailContract.Event.OnClickDismissNoticeToQuitDialog -> {
            onClickDismissNoticeToQuitDialog()
        }

        is BodyShapeDetailContract.Event.OnBottomSheetHide -> {
            onBottomSheetHide()
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun getBundle(): Bundle? {
        return savedStateHandle.get<Bundle>(BodyShapeDetailActivity.KEY_BUNDLE)
    }

    private val bodyShapeInfo: Pair<Int?, Int?> by lazy {
        Pair(
            getBundle()?.getInt(BodyShapeDetailActivity.KEY_ALBUM_ID) ?: 33,
            getBundle()?.getInt(BodyShapeDetailActivity.KEY_BODY_SHAPE_ID) ?: 1
        )
    }

    private fun onClickBodyShapeActionButton() {
        state.value.bodyShape?.author?.id?.let { bodyShapeAuthorId ->
            viewModelScope.launch {
                _state.update {
                    val bottomSheetButtonList = mutableListOf<BottomSheetButton>().apply {
                        add(BottomSheetButton.EDIT_POST)
                        add(BottomSheetButton.DELETE_POST)
                    }

                    it.copy(
                        bottomSheetButtonList = bottomSheetButtonList,
                        isBottomSheetShowing = true
                    )
                }
            }
        }
    }

    private fun onClickEditBodyShape() {
        bodyShapeInfo.first?.let { albumId ->
            bodyShapeInfo.second?.let { bodyShapeId ->
                viewModelScope.launch {
                    _effect.emit(BodyShapeDetailContract.Effect.GoToEdit(albumId, bodyShapeId))
                }
            }
        }
    }

    private fun onClickDeleteBodyShape() {
        bodyShapeInfo.first?.let { albumId ->
            bodyShapeInfo.second?.let { bodyShapeId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            isBottomSheetShowing = false
                        )
                    }

                    withContext(ioDispatcher) {
                        deleteBodyShapeUseCase(albumId, bodyShapeId).onEach {
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        isNoticeToQuitDialogShowing = true,
                                        noticeToQuitDialogContent = "삭제가 완료되었습니다."
                                    )
                                }
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }
                }
            }
        }
    }

    private fun getBodyShapeDetail() {
        bodyShapeInfo.first?.let { albumId ->
            bodyShapeInfo.second?.let { bodyShapeId ->
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }

                    withContext(ioDispatcher) {
                        getBodyShapeDetailUseCase(albumId, bodyShapeId).onEach { result ->
                            withContext(mainImmediateDispatcher) {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        bodyShape = result
                                    )
                                }
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }
                }
            }
        }
    }

    private fun onClickDismissNoticeDialog() {
        viewModelScope.launch {
            _state.update {
                it.copy(isNoticeDialogShowing = false)
            }
        }
    }

    private fun onClickDismissNoticeToQuitDialog() {
        viewModelScope.launch {
            _effect.emit(BodyShapeDetailContract.Effect.GoBack)
        }
    }

    private fun onBottomSheetHide() {
        viewModelScope.launch {
            _state.update {
                it.copy(isBottomSheetShowing = false)
            }
        }
    }
}