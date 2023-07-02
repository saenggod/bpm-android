package com.team.bpm.presentation.ui.main.mypage.notification

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.team.bpm.domain.model.Notification

class MyPageNotificationAdapter(
    private val listener: (Notification) -> Unit
) : PagingDataAdapter<Notification, MyPageNotificationViewHolder>(NotificationListDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPageNotificationViewHolder {
        return MyPageNotificationViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyPageNotificationViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item, listener)
        }
    }
}

private class NotificationListDiffUtil : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(
        oldItem: Notification,
        newItem: Notification
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Notification,
        newItem: Notification
    ): Boolean {
        return oldItem == newItem
    }
}