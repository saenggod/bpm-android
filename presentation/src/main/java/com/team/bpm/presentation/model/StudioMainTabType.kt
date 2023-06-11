package com.team.bpm.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class StudioMainTabType : Parcelable {
    POPULAR, REVIEW, NEW
}