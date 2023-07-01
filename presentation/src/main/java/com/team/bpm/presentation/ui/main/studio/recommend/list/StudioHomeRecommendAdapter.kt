package com.team.bpm.presentation.ui.main.studio.recommend.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.team.bpm.domain.model.Studio

class StudioHomeRecommendAdapter(
    private val listener: (Int?) -> Unit,
    private val scrapListener: (Int?, Boolean?) -> Unit
) : ListAdapter<Studio, StudioHomeRecommendViewHolder>(HomeRecommendListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudioHomeRecommendViewHolder {
        return StudioHomeRecommendViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: StudioHomeRecommendViewHolder, position: Int) {
        holder.bind(getItem(position), listener, scrapListener)
    }
}

private class HomeRecommendListDiffUtil : DiffUtil.ItemCallback<Studio>() {
    override fun areItemsTheSame(
        oldItem: Studio,
        newItem: Studio
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Studio,
        newItem: Studio
    ): Boolean {
        return oldItem == newItem
    }
}