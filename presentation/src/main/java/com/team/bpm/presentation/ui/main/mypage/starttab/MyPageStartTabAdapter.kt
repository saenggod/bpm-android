package com.team.bpm.presentation.ui.main.mypage.starttab

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.team.bpm.presentation.model.MainTabType

class MyPageStartTabAdapter(
    private val listener: (Int?) -> Unit
) : ListAdapter<MainTabType, MyPageStartTabViewHolder>(MyPageStartTabListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageStartTabViewHolder {
        return MyPageStartTabViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyPageStartTabViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

private class MyPageStartTabListDiffUtil : DiffUtil.ItemCallback<MainTabType>() {
    override fun areItemsTheSame(
        oldItem: MainTabType,
        newItem: MainTabType
    ): Boolean {
        return oldItem.isSelected == newItem.isSelected
    }

    override fun areContentsTheSame(
        oldItem: MainTabType,
        newItem: MainTabType
    ): Boolean {
        return oldItem == newItem
    }
}