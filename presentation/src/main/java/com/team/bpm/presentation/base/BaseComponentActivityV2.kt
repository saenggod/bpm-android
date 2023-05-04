package com.team.bpm.presentation.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.team.bpm.presentation.compose.theme.BPMTheme

abstract class BaseComponentActivityV2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BPMTheme {
                InitComposeUi()
            }
        }
    }

    @Composable
    protected abstract fun InitComposeUi()
}