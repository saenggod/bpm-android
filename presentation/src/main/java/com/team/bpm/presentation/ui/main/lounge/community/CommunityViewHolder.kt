package com.team.bpm.presentation.ui.main.lounge.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bpm.domain.model.Community
import com.team.bpm.presentation.databinding.ItemLoungeCommunityBinding
import com.team.bpm.presentation.util.bindImageUrl

class CommunityViewHolder(
    private val binding: ItemLoungeCommunityBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Community, listener: ((Int) -> Unit)) {
//        binding.item = item

        // TODO : Migrate to DataBinding
        if (!item.filesPath.isNullOrEmpty()) {
            item.filesPath?.get(0)?.let {
                binding.image.bindImageUrl(it)
            }
        }

        binding.root.setOnClickListener {
            item.id?.let { listener.invoke(it) }
        }
    }

    companion object {
        fun create(parent: ViewGroup): CommunityViewHolder {
            val binding = ItemLoungeCommunityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return CommunityViewHolder(binding)
        }
    }
}
