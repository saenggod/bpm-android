package com.team.bpm.presentation.ui.main.studio.schedule.select

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.BPMTextField
import com.team.bpm.presentation.compose.LoadingScreen
import com.team.bpm.presentation.compose.RoundedCornerButton
import com.team.bpm.presentation.compose.getLocalContext
import com.team.bpm.presentation.compose.theme.GrayColor2
import com.team.bpm.presentation.compose.theme.GrayColor5
import com.team.bpm.presentation.compose.theme.GrayColor9
import com.team.bpm.presentation.compose.theme.MainBlackColor
import com.team.bpm.presentation.compose.theme.MainGreenColor
import com.team.bpm.presentation.ui.main.studio.schedule.ScheduleActivity
import com.team.bpm.presentation.ui.main.studio.schedule.select.SelectStudioActivity.Companion.RESULT_OK
import com.team.bpm.presentation.util.clickableWithoutRipple
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SelectStudioActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        SelectStudioActivityContent()
    }

    companion object {
        const val RESULT_OK = 200

        fun newIntent(context: Context): Intent {
            return Intent(context, SelectStudioActivity::class.java)
        }
    }
}

@Composable
private fun SelectStudioActivityContent(
    viewModel: SelectStudioViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is SelectStudioContract.Effect.Finish -> {
                    context.setResult(RESULT_OK, Intent().putExtra(ScheduleActivity.KEY_STUDIO_NAME, effect.studioName))
                    context.finish()
                }
            }
        }
    }

    with(state) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            if (studioList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 56.dp,
                            bottom = 76.dp
                        )
                ) {
                    items(studioList) { studioList ->
//                        StudioWithCheckBox(
//                            studio = studioList,
//                            selectedStudioId = selectedStudio?.id ?: -1,
//                            onClick = { studio -> event.invoke(SelectStudioContract.Event.OnClickStudio(studio)) }
//                        )
                    }
                }
            }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(start = 14.dp)
                            .size(26.dp)
                            .align(CenterVertically)
                            .clickableWithoutRipple { context.finish() },
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = ""
                    )

                    val searchTextState = remember { mutableStateOf("") }

                    BPMTextField(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        textState = searchTextState,
                        label = null,
                        limit = null,
                        singleLine = true,
                        hint = null,
                        isExtendable = false,
                        minHeight = 40.dp,
                        iconPadding = 12.dp,
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            if (searchTextState.value.isNotEmpty()) {
                                event.invoke(
                                    SelectStudioContract.Event.OnClickSearch(
                                        searchTextState.value
                                    )
                                )
                            }
                        }),
                        icon = { hasFocus ->
                            Icon(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .size(32.dp)
                                    .align(Alignment.CenterEnd)
                                    .clickableWithoutRipple {
                                        if (searchTextState.value.isNotEmpty()) {
                                            event.invoke(
                                                SelectStudioContract.Event.OnClickSearch(
                                                    searchTextState.value
                                                )
                                            )
                                        }
                                    },
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "searchIconButton",
                                tint = if (hasFocus) GrayColor2 else GrayColor5
                            )
                        }
                    )
                }
            }

            if (isLoading) {
                LoadingScreen()
            }

            if (studioList.isNotEmpty()) {
                RoundedCornerButton(
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        )
                        .fillMaxWidth()
                        .height(48.dp)
                        .align(BottomCenter),
                    text = "선택 완료",
                    textColor = MainBlackColor,
                    buttonColor = MainGreenColor,
                    borderColor = if (selectedStudio != null) MainGreenColor else GrayColor9,
                    enabled = selectedStudio != null,
                    onClick = { event.invoke(SelectStudioContract.Event.OnClickComplete) }
                )
            }
        }
    }
}