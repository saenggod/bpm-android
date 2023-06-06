package com.team.bpm.presentation.ui.main.mypage.starttab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.R
import com.team.bpm.presentation.databinding.ItemHomeRecommendBinding
import com.team.bpm.presentation.databinding.ItemStartTabBinding
import com.team.bpm.presentation.model.MainTabType

class MyPageStartTabViewHolder(
    private val binding: ItemStartTabBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MainTabType, listener: (Int?) -> Unit) {
        with(binding) {
            this.item = item

            containerCard.setBackgroundResource(
                if (item.isSelected) {
                    R.drawable.bg_start_tab_selected
                } else {
                    R.drawable.bg_start_tab_unselected
                }
            )

            radio.setImageDrawable(
                if(item.isSelected) {
                    ContextCompat.getDrawable(root.context, R.drawable.ic_main_tab_radio_selected)
                } else {
                    ContextCompat.getDrawable(root.context, R.drawable.ic_main_tab_radio_unselected)
                }
            )

            icon.isSelected = item.isSelected

            root.setOnClickListener {
                listener.invoke(item.index)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): MyPageStartTabViewHolder {
            val binding = ItemStartTabBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return MyPageStartTabViewHolder(binding)
        }
    }
}
