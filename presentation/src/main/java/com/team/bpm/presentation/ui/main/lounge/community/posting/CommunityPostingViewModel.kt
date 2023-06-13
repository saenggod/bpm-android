package com.team.bpm.presentation.ui.main.lounge.community.posting

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.team.bpm.domain.usecase.community.WriteCommunityUseCase
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
class CommunityPostingViewModel @Inject constructor(private val writeCommunityUseCase: WriteCommunityUseCase) : BaseViewModelV2(), CommunityPostingContract {
    private val _state = MutableStateFlow(CommunityPostingContract.State())
    override val state: StateFlow<CommunityPostingContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CommunityPostingContract.Effect>()
    override val effect: SharedFlow<CommunityPostingContract.Effect> = _effect.asSharedFlow()

    override fun event(event: CommunityPostingContract.Event) = when (event) {
        is CommunityPostingContract.Event.OnClickImagePlaceHolder -> {
            onClickImagePlaceHolder()
        }

        is CommunityPostingContract.Event.OnClickRemoveImage -> {
            onClickRemoveImage(event.index)
        }

        is CommunityPostingContract.Event.OnImagesAdded -> {
            onImagesAdded(event.images)
        }

        is CommunityPostingContract.Event.OnClickSubmit -> {
            onClickSubmit(event.content)
        }
    }

    private val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { coroutineContext, throwable ->

        }
    }

    private fun onClickImagePlaceHolder() {
        viewModelScope.launch {
            _effect.emit(CommunityPostingContract.Effect.AddImages)
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

    private fun onClickSubmit(content: String) {
        viewModelScope.launch {
            if (state.value.imageList.isNotEmpty()) {
                if (content.isNotEmpty()) {
                    _state.update {
                        it.copy(isLoading = true)
                    }

                    withContext(ioDispatcher) {
                        writeCommunityUseCase(content, state.value.imageList.map { image -> convertImageBitmapToByteArray(image.second) }).onEach { result ->
                            withContext(mainImmediateDispatcher) {
                                result.id?.let { communityId ->
                                    _effect.emit(
                                        CommunityPostingContract.Effect.RedirectToCommunity(
                                            communityId
                                        )
                                    )
                                }
                            }
                        }.launchIn(viewModelScope + exceptionHandler)
                    }
                } else {
                    _effect.emit(CommunityPostingContract.Effect.ShowToast("내용을 입력해주세요."))
                }
            } else {
                _effect.emit(CommunityPostingContract.Effect.ShowToast("사진을 추가해주세요."))
            }
        }
    }
}