package com.team.bpm.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.team.bpm.presentation.compose.theme.GrayColor16
import com.team.bpm.presentation.compose.theme.MainBlackColor
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
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0f)
        block()
    }
}

@Composable
fun NoticeDialog(
    title: String?,
    content: String,
    confirmButtonText: String = "확인",
    onClickConfirm: (() -> Unit)?
) {
    BaseComposableDialog(
        isCancelable = false,
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
                    .align(Alignment.End)
                    .width(41.dp)
                    .height(34.dp)
                    .clickableWithoutRipple { onClickConfirm?.invoke() }
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