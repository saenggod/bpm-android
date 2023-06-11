package com.team.bpm.presentation.model

import androidx.annotation.DrawableRes
import com.team.bpm.presentation.R

enum class MainBanner(@DrawableRes val imgRes: Int) {
    REGISTER(R.drawable.img_banner_register),
    WRITE(R.drawable.img_banner_write),
    LOUNGE(R.drawable.img_banner_lounge);
}