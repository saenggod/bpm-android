package com.team.bpm.presentation.ui.main.lounge.community

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.team.bpm.domain.model.Community

class CommunityAdapter(
    private val listener: (Int) -> Unit
) : ListAdapter<Community, CommunityViewHolder>(CommunityListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        return CommunityViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

private class CommunityListDiffUtil : DiffUtil.ItemCallback<Community>() {
    override fun areItemsTheSame(
        oldItem: Community,
        newItem: Community
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Community,
        newItem: Community
    ): Boolean {
        return oldItem == newItem
    }
}