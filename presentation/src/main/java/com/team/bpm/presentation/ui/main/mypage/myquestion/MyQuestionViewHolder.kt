package com.team.bpm.presentation.ui.main.mypage.myquestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bpm.domain.model.Question
import com.team.bpm.presentation.databinding.ItemLoungeQuestionBinding

class MyQuestionViewHolder(
    private val binding: ItemLoungeQuestionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Question, listener: ((Int) -> Unit)) {
        binding.item = item

        binding.root.setOnClickListener {
            item.id?.let { listener.invoke(it) }
        }
    }

    companion object {
        fun create(parent: ViewGroup): MyQuestionViewHolder {
            val binding = ItemLoungeQuestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return MyQuestionViewHolder(binding)
        }
    }
}
