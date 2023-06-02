package com.team.bpm.presentation.ui.main.lounge.question

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.team.bpm.domain.model.Question

class QuestionAdapter(
    private val listener: (Int) -> Unit
) : ListAdapter<Question, QuestionViewHolder>(QuestionListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
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