package com.team.bpm.presentation.ui.main.studio.recommend.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.databinding.ItemHomeRecommendBinding

class StudioHomeRecommendViewHolder(
    private val binding: ItemHomeRecommendBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Studio, listener: (Int?) -> Unit) {
        with(binding) {
            this.item = item

            list.adapter = StudioHomeRecommendImageAdapter()

            root.setOnClickListener {
                listener.invoke(item.id)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): StudioHomeRecommendViewHolder {
            val binding = ItemHomeRecommendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return StudioHomeRecommendViewHolder(binding)
        }
    }
}
