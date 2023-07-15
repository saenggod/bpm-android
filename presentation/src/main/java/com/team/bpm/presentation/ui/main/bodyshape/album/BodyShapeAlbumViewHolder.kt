package com.team.bpm.presentation.ui.main.bodyshape.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bpm.domain.model.BodyShapeDetail
import com.team.bpm.presentation.databinding.ItemBodyshapeAlbumBinding
import com.team.bpm.presentation.util.bindImageUrl
import org.joda.time.Days

class BodyShapeAlbumViewHolder(
    private val binding: ItemBodyshapeAlbumBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BodyShapeDetail, listener: ((Int) -> Unit)) {
        val datePrefix = if ((item.dday ?: 0) >= 0) "+" else ""

        binding.image.bindImageUrl(item.imagePath)
        binding.label.text = "D${datePrefix}${item.dday}"

        binding.root.setOnClickListener {
            item.id.let(listener)
        }

    }

    companion object {
        fun create(parent: ViewGroup): BodyShapeAlbumViewHolder {
            val binding = ItemBodyshapeAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return BodyShapeAlbumViewHolder(binding)
        }
    }
}