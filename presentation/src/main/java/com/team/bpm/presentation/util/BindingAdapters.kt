package com.team.bpm.presentation.util

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.team.bpm.presentation.R
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat

@BindingAdapter("bind:visibility")
fun View.bindVisibleOrGone(isVisibleOrGone: Boolean?) {
    visibility = if (isVisibleOrGone == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("bind:visibility")
fun View.bindVisibleOrGone(text: String?) {
    visibility = if (!text.isNullOrEmpty()) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("bind:selected")
fun View.bindIsSelected(isSelected: Boolean?) {
    this.isSelected = isSelected ?: false
}

@BindingAdapter("bind:list_item")
fun RecyclerView.bindListItems(list: List<Any>?) {
    if (adapter != null) {
        (adapter as ListAdapter<Any, *>).submitList(list ?: emptyList())
    }
}

@BindingAdapter("bind:review_chips")
fun ChipGroup.bindChips(reviewList: List<String>?) {
    if (reviewList.isNullOrEmpty()) return else {
        isClickable = false
        reviewList.forEach {
            addView(
                Chip(context).apply {
                    text = it
                    setChipBackgroundColorResource(R.color.gray_12)
                }
            )
        }
    }
}

@BindingAdapter("bind:image_url")
fun ImageView.bindImageUrl(url: String?) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .thumbnail(0.1f)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}


@BindingAdapter("bind:image_src")
fun ImageView.bindImageSrc(@DrawableRes src: Int?) {
    Glide.with(this.context)
        .load(src)
        .centerCrop()
        .into(this)
}

@BindingAdapter("bind:home_user_album_date", "bind:home_user_album_time")
fun AppCompatTextView.bindHomeAlbum(dateString: String?, timeString: String?) {
    if (dateString.isNullOrEmpty() || timeString.isNullOrEmpty() || dateString == "-" || timeString == "-") return
    else {
        val date = DateTime.parse(dateString, DateTimeFormat.forPattern("yyyy-MM-dd"))
        val time = DateTime.parse(timeString, DateTimeFormat.forPattern("HH:mm:ss"))

        text = "${date.toString("yyyy년 MM월 dd일 ")} ${time.getKoreanHour()}"
    }
}

@BindingAdapter("bind:home_user_album_dday")
fun AppCompatTextView.bindHomeAlbumDday(dateString: String?) {
    if (dateString.isNullOrEmpty() || dateString == "-") return
    else {
        val date = DateTime.parse(dateString, DateTimeFormat.forPattern("yyyy-MM-dd"))
        text = "D${Days.daysBetween(date, DateTime.now()).days}"
    }
}

@BindingAdapter("bind:bodyshape_dday")
fun AppCompatTextView.bindBodyShapeDday(dday: Int?) {
    dday?.let { dday ->
        val datePrefix = if ((dday ?: 0) > 0) "+" else ""
        text = "D${datePrefix}${dday}"
    }
}

@BindingAdapter("bind:bodyshape_date_range_start","bind:bodyshape_date_range_end")
fun AppCompatTextView.bindBodyShapeDateRange(createAt : String?, dueDate : String?) {

    if(createAt.isNullOrEmpty() || dueDate.isNullOrEmpty()) return
    else {
        text = "$createAt ~ $dueDate"
    }
}

@BindingAdapter("bind:bodyshape_history_count")
fun AppCompatTextView.bindBodyShapeHistoryCount(count : Int?) {
    count?.let {
        text = "기록 $count"
    }
}
