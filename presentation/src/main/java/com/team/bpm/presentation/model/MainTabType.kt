package com.team.bpm.presentation.model

import androidx.annotation.DrawableRes
import com.team.bpm.presentation.R

class MainTabType(
    var isSelected : Boolean = false,
    var index: Int,
    var name: String,
    @DrawableRes var image: Int
) {

    override fun equals(other: Any?): Boolean {
        return isSelected == (other as? MainTabType)?.isSelected
    }

    companion object {
        var tabList = listOf(
            MainTabType(
                index = 0,
                name = "스튜디오",
                image = R.drawable.navigation_home
            ),
            MainTabType(
                index = 1,
                name = "라운지",
                image = R.drawable.navigation_lounge
            ),
            MainTabType(
                index = 2,
                name = "눈바디",
                image = R.drawable.navigation_body_shape
            )
        )
    }
}