package com.team.bpm.presentation.ui.main.lounge.question

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes

@BindingAdapter("bind:time_before_now")
fun AppCompatTextView.bindCalculatingTimeBeforeNow(nowTimeString: String? = null) {
    val nowTime = DateTime.now()

    val time = if (nowTimeString.isNullOrEmpty()) {
        nowTime
    } else {
        DateTime.parse(nowTimeString)
    }

    val dayDiff = Days.daysBetween(time, nowTime).days
    val hourDiff = Hours.hoursBetween(time, nowTime).hours
    val minDiff = Minutes.minutesBetween(time, nowTime).minutes

    text = when {
        dayDiff > 0 -> {
            time.toString("yyyy.MM.dd")
        }

        hourDiff > 0 -> {
            "${hourDiff}시간전"
        }

        minDiff > 0 -> {
            "${minDiff}분전"
        }

        else -> {
            "방금 전"
        }
    }
}
