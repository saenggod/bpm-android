package com.team.bpm.presentation.base

import androidx.lifecycle.ViewModel
import com.team.bpm.presentation.di.IoDispatcher
import com.team.bpm.presentation.di.MainImmediateDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class BaseViewModelV2 : ViewModel() {
    @Inject
    @MainImmediateDispatcher
    lateinit var mainImmediateDispatcher: CoroutineDispatcher

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher
}