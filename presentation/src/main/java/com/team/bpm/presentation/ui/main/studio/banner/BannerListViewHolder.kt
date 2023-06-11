package com.team.bpm.presentation.ui.main.studio.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bpm.presentation.databinding.ItemBannerBinding
import com.team.bpm.presentation.databinding.ItemHomeRecommendImageBinding
import com.team.bpm.presentation.model.MainBanner
import com.team.bpm.presentation.util.bindImageSrc

class BannerListViewHolder(
    private val binding: ItemBannerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MainBanner, itemClick: ((type: String) -> Unit)) {
        binding.banner.bindImageSrc(item.imgRes)
        binding.root.setOnClickListener {
            itemClick.invoke(item.name)
        }
    }

    companion object {
        fun create(parent: ViewGroup): BannerListViewHolder {
            val binding = ItemBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return BannerListViewHolder(binding)
        }
    }
}
