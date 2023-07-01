package com.team.bpm.presentation.ui.main.bodyshape.album

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.team.bpm.domain.model.BodyShapeDetail

class BodyShapeAlbumAdapter(
    private val listener: (Int) -> Unit,
) : ListAdapter<BodyShapeDetail, BodyShapeAlbumViewHolder>(BodyShapeAlbumListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyShapeAlbumViewHolder {
        return BodyShapeAlbumViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BodyShapeAlbumViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

private class BodyShapeAlbumListDiffUtil : DiffUtil.ItemCallback<BodyShapeDetail>() {
    override fun areItemsTheSame(
        oldItem: BodyShapeDetail,
        newItem: BodyShapeDetail
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: BodyShapeDetail,
        newItem: BodyShapeDetail
    ): Boolean {
        return oldItem == newItem
    }
}