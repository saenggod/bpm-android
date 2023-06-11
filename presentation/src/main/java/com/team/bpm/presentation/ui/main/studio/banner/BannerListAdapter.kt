package com.team.bpm.presentation.ui.main.studio.banner

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.team.bpm.presentation.model.MainBanner

class BannerListAdapter(
    private val listener: ((link: String) -> Unit)
) : ListAdapter<MainBanner, BannerListViewHolder>(BannerListDiffUtil()) {

//    override fun getItemCount(): Int = if (currentList.size < 2) currentList.size else currentList.size * 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerListViewHolder {
        return BannerListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BannerListViewHolder, position: Int) {
        holder.bind(getItem(position % currentList.size), listener)
    }
}

private class BannerListDiffUtil : DiffUtil.ItemCallback<MainBanner>() {
    override fun areItemsTheSame(
        oldItem: MainBanner,
        newItem: MainBanner
    ): Boolean {
        return oldItem.imgRes == newItem.imgRes
    }

    override fun areContentsTheSame(
        oldItem: MainBanner,
        newItem: MainBanner
    ): Boolean {
        return oldItem == newItem
    }
}