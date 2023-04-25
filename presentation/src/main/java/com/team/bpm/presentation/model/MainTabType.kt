package com.team.bpm.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MainTabType : Parcelable {
    HOT, REVIEW, OPEN
}