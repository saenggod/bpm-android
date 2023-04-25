package com.team.bpm.presentation.ui.main.studio.recommend.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class StudioHomeRecommendImageAdapter(
) : ListAdapter<String, StudioHomeRecommendImageViewHolder>(HomeRecommendImageListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudioHomeRecommendImageViewHolder {
        return StudioHomeRecommendImageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: StudioHomeRecommendImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class HomeRecommendImageListDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }
}