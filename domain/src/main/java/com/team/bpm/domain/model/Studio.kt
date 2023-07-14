package com.team.bpm.domain.model

import com.team.bpm.domain.base.BaseModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.math.round

@Parcelize
data class Studio(
    val id: Int?,
    val name: String?,
    val address: String?,
    val addressDescription: String?,
    val latitude: Double?,
    val longitude: Double?,
    val firstTag: String?,
    val secondTag: String?,
    val topRecommends: List<Pair<String, Int>>?,
    val phone: String?,
    val sns: String?,
    val openHours: String?,
    val price: String?,
    val filesPath: List<String>?,
    val content: String?,
    val rating: Double?,
    val reviewCount: Int?,
    val scrapCount: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val scrapped: Boolean?
) : BaseModel {

    @IgnoredOnParcel
    val tagList = arrayListOf<String>().apply {
        firstTag?.let { add(it) }
        secondTag?.let { add(it) }
    }
}