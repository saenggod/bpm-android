package com.team.bpm.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.team.bpm.presentation.compose.theme.GrayColor13
import com.team.bpm.presentation.compose.theme.GrayColor7
import com.team.bpm.presentation.compose.theme.MainGreenColor
import com.team.bpm.presentation.util.clickableWithoutRipple

@Composable
inline fun BaseComposableDialog(
    isCancelable: Boolean,
    crossinline block: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = isCancelable,
            dismissOnClickOutside = isCancelable
        )
    ) {
        block()
    }
}

@Composable
inline fun NoticeDialog(
    isCancelable: Boolean,
    text: String,
    crossinline onClickConfirm: () -> Unit
) {
    BaseComposableDialog(
        isCancelable = isCancelable,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .width(260.dp)
                .height(160.dp)
                .background(color = Color.White),
            horizontalAlignment = CenterHorizontally
        ) {
            Box(modifier = Modifier.height(40.dp)) {
                Text(
                    modifier = Modifier.align(Center),
                    text = "알림",
                    fontWeight = SemiBold,
                    fontSize = 16.sp,
                    letterSpacing = 0.sp
                )
            }

            Divider(
                thickness = 1.dp,
                color = GrayColor13
            )

            Box(modifier = Modifier.height(80.dp)) {
                Text(
                    modifier = Modifier.align(Center),
                    text = text,
                    fontWeight = Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp
                )
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(color = MainGreenColor)
                .clickable { onClickConfirm() }
            ) {
                Text(
                    modifier = Modifier
                        .align(Center),
                    text = "확인",
                    fontWeight = SemiBold,
                    fontSize = 16.sp,
                    letterSpacing = 0.sp
                )
            }
        }
    }
}