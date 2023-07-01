package com.team.bpm.presentation.ui.main.studio.recommend.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.team.bpm.domain.model.Studio
import com.team.bpm.presentation.R
import com.team.bpm.presentation.databinding.ItemHomeRecommendBinding
import com.team.bpm.presentation.util.bindImageSrc

class StudioHomeRecommendViewHolder(
    private val binding: ItemHomeRecommendBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Studio, listener: (Int?) -> Unit, scrapListener: (Int?, Boolean?) -> Unit) {
        with(binding) {
            this.item = item

            list.adapter = StudioHomeRecommendImageAdapter()

            item.scrapped?.let { isScrapped ->
                scrap.bindImageSrc(
                    if (isScrapped) {
                        R.drawable.ic_home_scrap_filled
                    } else {
                        R.drawable.ic_home_scrap
                    }
                )
            }

            chipGroup.removeAllViews()

            item.tagList.forEach {
                chipGroup.addView(
                    Chip(chipGroup.context).apply {
                        text = it
                        setChipBackgroundColorResource(R.color.gray_12)
                    }
                )
            }

            container.setOnClickListener {
                listener.invoke(item.id)
            }

            scrap.setOnClickListener {
                item.scrapped?.let { isScrapped ->
                    scrap.bindImageSrc(
                        if (isScrapped) {
                            R.drawable.ic_home_scrap
                        } else {
                            R.drawable.ic_home_scrap_filled
                        }
                    )
                }

                scrapListener.invoke(item.id, item.scrapped)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): StudioHomeRecommendViewHolder {
            val binding = ItemHomeRecommendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return StudioHomeRecommendViewHolder(binding)
        }
    }
}
