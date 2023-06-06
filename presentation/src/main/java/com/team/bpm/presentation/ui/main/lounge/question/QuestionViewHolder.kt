package com.team.bpm.presentation.ui.main.lounge.question

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bpm.domain.model.Community
import com.team.bpm.domain.model.Question
import com.team.bpm.presentation.databinding.ItemLoungeCommunityBinding
import com.team.bpm.presentation.databinding.ItemLoungeQuestionBinding
import com.team.bpm.presentation.util.bindImageUrl

class QuestionViewHolder(
    private val binding: ItemLoungeQuestionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Question, listener: ((Int) -> Unit)) {
        binding.item = item

        binding.root.setOnClickListener {
            item.id?.let { listener.invoke(it) }
        }
    }

    companion object {
        fun create(parent: ViewGroup): QuestionViewHolder {
            val binding = ItemLoungeQuestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return QuestionViewHolder(binding)
        }
    }
}
