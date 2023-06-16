package com.team.bpm.presentation.ui.main.mypage.myquestion

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.team.bpm.presentation.R

@BindingAdapter("bind:my_question_selected")
fun ConstraintLayout.bindMyQuestionIsSelected(isSelected: Boolean?) {
    isSelected?.let {
        setBackgroundColor(
            if (it) {
                ContextCompat.getColor(context, R.color.green_A8FF0F_10)
            } else {
                ContextCompat.getColor(context, R.color.white)
            }
        )
    }
}