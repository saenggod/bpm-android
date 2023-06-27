package com.team.bpm.presentation.ui.main.bodyshape.album

import androidx.lifecycle.viewModelScope
import com.team.bpm.presentation.base.BaseViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BodyShapeAlbumViewModel @Inject constructor(
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel(), BodyShapeAlbumContract {

    private val _state = MutableStateFlow(BodyShapeAlbumContract.State())
    override val state: StateFlow<BodyShapeAlbumContract.State> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BodyShapeAlbumContract.Effect>()
    override val effect: SharedFlow<BodyShapeAlbumContract.Effect> = _effect.asSharedFlow()

    override fun event(event: BodyShapeAlbumContract.Event) {
        viewModelScope.launch(mainImmediateDispatcher) {
//            when (event) {
//
//            }
        }
    }

}