package com.team.bpm.presentation.util

import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes

fun String.calculatedFromNow(): String {
    val currentTime = DateTime.now()

    val time = if (this.isEmpty()) {
        currentTime
    } else {
        DateTime.parse(this)
    }

    val dayDiff = Days.daysBetween(time, currentTime).days
    val hourDiff = Hours.hoursBetween(time, currentTime).hours
    val minDiff = Minutes.minutesBetween(time, currentTime).minutes

    val showingTime = when {
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

    return showingTime
}

fun Double.clip(): String {
    return this.toString().substring(0..2)
}