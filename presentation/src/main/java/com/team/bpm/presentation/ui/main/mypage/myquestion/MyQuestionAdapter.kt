package com.team.bpm.presentation.ui.main.mypage.myquestion

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.team.bpm.domain.model.Question

class MyQuestionAdapter(
    private val listener: (Int) -> Unit
) : ListAdapter<Question, MyQuestionViewHolder>(QuestionListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyQuestionViewHolder {
        return MyQuestionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyQuestionViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

private class QuestionListDiffUtil : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(
        oldItem: Question,
        newItem: Question
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Question,
        newItem: Question
    ): Boolean {
        return oldItem == newItem
    }
}