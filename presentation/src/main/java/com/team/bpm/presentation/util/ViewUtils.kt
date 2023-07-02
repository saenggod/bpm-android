package com.team.bpm.presentation.util

import android.content.res.Resources
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.TypedValue
import org.joda.time.DateTime

val Int.dp: Int
    get() {
        val metrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics)
            .toInt()
    }

fun DateTime.getKoreanHour(): String {
    val nowHour = this.hourOfDay
    return if (nowHour < 12) "오전 ${nowHour}시"
    else "오후 ${nowHour - 12}시"
}

fun String.highLightWord(forceWord: String): SpannableString {
    return if (this.contains(forceWord)) {
        SpannableString(this).apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                this.indexOf(forceWord),
                this.indexOf(forceWord) + forceWord.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    } else {
        SpannableString(this)
    }
}
