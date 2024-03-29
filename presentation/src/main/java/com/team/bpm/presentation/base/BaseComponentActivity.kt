package com.team.bpm.presentation.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.team.bpm.presentation.compose.LoadingScreen
import com.team.bpm.presentation.compose.theme.BPMTheme

abstract class BaseComponentActivity : ComponentActivity() {

    protected abstract val viewModel: BaseViewModel
    private val loadingVisibilityState = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUi()
        setupCollect()
    }

    protected open fun initUi() {}

    protected fun initComposeUi(block: @Composable () -> Unit) {
        setContent {
            BPMTheme {
                block()

                if (loadingVisibilityState.value) {
                    LoadingScreen()
                }
            }
        }
    }

    protected fun showLoadingScreen() {
        loadingVisibilityState.value = true
    }

    protected fun hideLoadingScreen() {
        loadingVisibilityState.value = false
    }

    protected open fun setupCollect() {}
}