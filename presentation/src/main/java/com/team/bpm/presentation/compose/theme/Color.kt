package com.team.bpm.presentation.compose.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Transparent = Color(0x00000000)

val MainBlackColor = Color(0xFF1B1B1B)
val MainGreenColor = Color(0xFFA8FF0F)
val SubGreenColor = Color(0xFFB2FF0E)
val PointRedColor = Color(0xFFFF2E00)
val HighlightColor = Color(0x26A8FF0F)

val GrayColor0 = Color(0xFF171717)
val GrayColor1 = Color(0xFF292929)
val GrayColor2 = Color(0xFF3E3E3E)
val GrayColor3 = Color(0xFF595959)
val GrayColor4 = Color(0xFF838383)
val GrayColor5 = Color(0xFF9EA0A4)
val GrayColor6 = Color(0xFFBFBFBF)
val GrayColor7 = Color(0xFFD1D1D3)
val GrayColor8 = Color(0xFFE6E6E7)
val GrayColor9 = Color(0xFFF0F0F1)
val GrayColor10 = Color(0xFFF4F6F6)
val GrayColor11 = Color(0xFFF5F6F6)
val GrayColor12 = Color(0x80000000)
val GrayColor13 = Color(0xFFEEEEEE)
val GrayColor14 = Color(0xCCAFAFAF)
val GrayColor15 = Color(0xFFD7D7D8)
val GrayColor16 = Color(0x99000000)
val FilteredWhiteColor = Color(0xB3FFFFFF)

@Composable
fun textSelectionColor() = TextSelectionColors(
    handleColor = MainBlackColor,
    backgroundColor = GrayColor6
)

@Composable
fun textFieldColors() = TextFieldDefaults.textFieldColors(
    textColor = MainBlackColor,
    backgroundColor = Color.White,
    cursorColor = MainBlackColor,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
)