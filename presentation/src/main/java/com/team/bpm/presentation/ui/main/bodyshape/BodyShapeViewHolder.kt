package com.team.bpm.presentation.ui.main.bodyshape

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.team.bpm.domain.model.BodyShapeSchedule
import com.team.bpm.presentation.R
import com.team.bpm.presentation.databinding.ItemBodyshapeBinding
import com.team.bpm.presentation.util.bindImageUrl
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class BodyShapeViewHolder(
    private val binding: ItemBodyshapeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: BodyShapeSchedule,
        listener: ((Int) -> Unit),
        imageClickListener: ((Int, Int?) -> Unit)
    ) {
        binding.item = item

        with(binding) {
            val dateObject = DateTime.parse(item.date, DateTimeFormat.forPattern("yyyy-MM-dd"))
            val datePrefix = if (item.dday >= 0) "+" else ""

            dDay.text = "D${datePrefix}${item.dday}"
            date.text = dateObject.toString("yyyy.MM.dd")
            name.text = item.scheduleName

            labelToday.text = if (item.dday > 0) {
                "완료"
            } else {
                "오늘"
            }

            val containerBackground = if (item.dday > 0) {
                AppCompatResources.getDrawable(root.context, R.drawable.bg_bodyshape_last)
            } else if (item.dday > -30) {
                AppCompatResources.getDrawable(root.context, R.drawable.bg_bodyshape_second)
            } else {
                AppCompatResources.getDrawable(root.context, R.drawable.bg_bodyshape_first)
            }

            container.background = containerBackground

            if (item.isTodayPost == true) {
                image.bindImageUrl(item.imagePath)
                labelToday.isVisible = true
            }
        }

        binding.container.setOnClickListener {
            item.id?.let(listener)
        }

        binding.containerImage.setOnClickListener {
            if (!item.imagePath.isNullOrEmpty()) {
                item.id?.let { albumId ->
                    val bodyShapeDetailId = item.bodyShapeList?.bodyShapeDetails?.firstOrNull()?.id
                    imageClickListener.invoke(albumId, if(item.isTodayPost == true) bodyShapeDetailId else null)
                }
            } else {
                item.id?.let(listener)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): BodyShapeViewHolder {
            val binding = ItemBodyshapeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return BodyShapeViewHolder(binding)
        }
    }
}