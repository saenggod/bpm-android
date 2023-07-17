package com.team.bpm.presentation.ui.main.bodyshape

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.team.bpm.domain.model.BodyShapeSchedule

class BodyShapeAdapter(
    private val listener: (Int) -> Unit,
    private val imageClickListener: (Int, Int?) -> Unit,
) : ListAdapter<BodyShapeSchedule, BodyShapeViewHolder>(BodyShapeListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyShapeViewHolder {
        return BodyShapeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BodyShapeViewHolder, position: Int) {
        holder.bind(getItem(position), listener, imageClickListener)
    }
}

private class BodyShapeListDiffUtil : DiffUtil.ItemCallback<BodyShapeSchedule>() {
    override fun areItemsTheSame(
        oldItem: BodyShapeSchedule,
        newItem: BodyShapeSchedule
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: BodyShapeSchedule,
        newItem: BodyShapeSchedule
    ): Boolean {
        return oldItem == newItem
    }
}