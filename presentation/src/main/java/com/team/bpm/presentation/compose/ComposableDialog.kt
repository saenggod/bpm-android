package com.team.bpm.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.team.bpm.presentation.compose.theme.GrayColor16
import com.team.bpm.presentation.compose.theme.GrayColor5
import com.team.bpm.presentation.compose.theme.MainBlackColor
import com.team.bpm.presentation.util.clickableWithoutRipple

@Composable
inline fun BaseComposableDialog(
    crossinline onDismissRequest: () -> Unit,
    crossinline block: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0f)
        block()
    }
}

@Composable
fun NoticeDialog(
    title: String?,
    content: String,
    confirmButtonText: String = "확인",
    onDismissRequest: () -> Unit
) {
    BaseComposableDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Box(
            modifier = Modifier
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .width(280.dp)
                .heightIn(min = 115.dp)
                .background(color = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        top = 23.dp,
                        start = 24.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    )
                    .width(248.dp)
            ) {
                title?.let { title ->
                    Text(
                        modifier = Modifier.height(24.dp),
                        text = title,
                        fontWeight = Medium,
                        fontSize = 15.sp,
                        letterSpacing = 0.sp,
                        color = MainBlackColor
                    )
                }

                Text(
                    modifier = Modifier
                        .width(232.dp)
                        .heightIn(min = 42.dp),
                    text = content,
                    fontWeight = Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    color = GrayColor16,
                )

                BPMSpacer(height = 8.dp)

                Box(modifier = Modifier
                    .align(End)
                    .width(41.dp)
                    .height(34.dp)
                    .clickableWithoutRipple { onDismissRequest() }
                ) {
                    Text(
                        text = confirmButtonText,
                        fontWeight = Medium,
                        fontSize = 14.sp,
                        letterSpacing = 0.sp,
                        color = MainBlackColor
                    )
                }
            }
        }
    }
}

@Composable
inline fun TextFieldDialog(
    title: String,
    hint: String = "내용을 입력해주세요.",
    cancelButtonText: String = "취소",
    confirmButtonText: String = "확인",
    focusRequester: FocusRequester,
    crossinline onDismissRequest: () -> Unit,
    crossinline onClickCancel: () -> Unit,
    crossinline onClickConfirm: (String) -> Unit
) {
    BaseComposableDialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Box(
            modifier = Modifier
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .width(280.dp)
                .height(274.dp)
                .background(color = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        top = 24.dp,
                        start = 16.dp,
                        end = 16.dp,
                    )
                    .width(248.dp)
            ) {
                Text(
                    modifier = Modifier.align(CenterHorizontally),
                    text = title,
                    fontWeight = Medium,
                    fontSize = 16.sp,
                    letterSpacing = 0.sp,
                    color = MainBlackColor
                )

                BPMSpacer(height = 20.dp)

                val contentTextState = remember { mutableStateOf("") }

                BPMTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    textState = contentTextState,
                    minHeight = 140.dp,
                    label = null,
                    limit = null,
                    singleLine = false,
                    hint = hint,
                    isExtendable = false
                )

                BPMSpacer(height = 24.dp)

                Row(
                    modifier = Modifier.align(End)
                ) {
                    Box(
                        modifier = Modifier
                            .width(41.dp)
                            .height(34.dp)
                            .clickableWithoutRipple { onClickCancel() }
                    ) {
                        Text(
                            modifier = Modifier.align(Center),
                            text = cancelButtonText,
                            fontWeight = Medium,
                            fontSize = 14.sp,
                            letterSpacing = 0.sp,
                            color = GrayColor5
                        )
                    }

                    BPMSpacer(width = 8.dp)

                    Box(
                        modifier = Modifier
                            .width(41.dp)
                            .height(34.dp)
                            .clickableWithoutRipple {
                                if (contentTextState.value.isNotEmpty()) {
                                    onClickConfirm(contentTextState.value)
                                }
                            }
                    ) {
                        Text(
                            modifier = Modifier.align(Center),
                            text = confirmButtonText,
                            fontWeight = Medium,
                            fontSize = 14.sp,
                            letterSpacing = 0.sp,
                            color = MainBlackColor
                        )
                    }
                }

                BPMSpacer(height = 8.dp)
            }
        }
    }
}