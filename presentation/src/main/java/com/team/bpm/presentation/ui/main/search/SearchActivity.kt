package com.team.bpm.presentation.ui.main.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.bpm.presentation.R
import com.team.bpm.presentation.base.BaseComponentActivityV2
import com.team.bpm.presentation.base.use
import com.team.bpm.presentation.compose.BPMSpacer
import com.team.bpm.presentation.compose.getLocalContext
import com.team.bpm.presentation.compose.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseComponentActivityV2() {
    @Composable
    override fun InitComposeUi() {
        SearchActivityContent()
    }
}

@Composable
private fun SearchActivityContent(viewModel: SearchViewModel = hiltViewModel()) {
    val (state, event, effect) = use(viewModel)
    val context = getLocalContext()
    val searchTextFieldState = remember { mutableStateOf("") }

    with(state) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(
                                start = 6.dp,
                                end = 6.dp,
                                bottom = 7.dp
                            )
                            .align(BottomCenter)
                            .fillMaxWidth()
                            .height(36.dp)
                    ) {
                        Box(modifier = Modifier.size(36.dp)) {
                            Icon(
                                modifier = Modifier.align(Center),
                                painter = painterResource(id = R.drawable.ic_arrow_back2),
                                contentDescription = "backButtonIcon",
                                tint = GrayColor1
                            )
                        }

                        CompositionLocalProvider(LocalTextSelectionColors.provides(textSelectionColor())) {
                            Box(
                                modifier = Modifier
                                    .align(CenterVertically)
                                    .weight(1f)
                            ) {
                                if (searchTextFieldState.value.isEmpty()) {
                                    Text(
                                        text = "검색어를 입력해주세요",
                                        fontSize = 14.sp,
                                        letterSpacing = 0.sp,
                                        color = GrayColor6
                                    )
                                }

                                BasicTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = searchTextFieldState.value,
                                    onValueChange = { searchTextFieldState.value = it },
                                    singleLine = true,
                                    keyboardActions = KeyboardActions(onDone = { }),
                                    cursorBrush = SolidColor(MainBlackColor),
                                    textStyle = TextStyle(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        letterSpacing = 0.sp,
                                        color = MainBlackColor
                                    )
                                )
                            }
                        }

                        Box(modifier = Modifier.size(36.dp)) {
                            Icon(
                                modifier = Modifier.align(Center),
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "searchButtonIcon",
                                tint = GrayColor1
                            )
                        }
                    }
                }

                Divider(color = GrayColor2)

                Column(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 30.dp
                        )
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "최근 검색어",
                        fontWeight = Bold,
                        fontSize = 14.sp,
                        letterSpacing = 0.sp,
                        color = GrayColor4
                    )

                    BPMSpacer(height = 8.dp)

                    // 최근 검색어
                }
            }
        }
    }
}



