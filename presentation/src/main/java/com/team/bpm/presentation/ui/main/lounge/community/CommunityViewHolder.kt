package com.team.bpm.presentation.ui.main.lounge.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bpm.presentation.databinding.ItemHomeRecommendImageBinding

class CommunityViewHolder(
    private val binding: ItemHomeRecommendImageBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
        binding.item = item
    }

    companion object {
        fun create(parent: ViewGroup): CommunityViewHolder {
            val binding = ItemHomeRecommendImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return CommunityViewHolder(binding)
        }
    }
}
