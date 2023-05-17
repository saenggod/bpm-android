package com.team.bpm.data.model.response

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.Studio
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class StudioResponse(
    @SerializedName(value = "id")
    val id: Int?,
    @SerializedName(value = "name")
    val name: String?,
    @SerializedName(value = "address")
    val address: String?,
    @SerializedName(value = "latitude")
    val latitude: Double?,
    @SerializedName(value = "longitude")
    val longitude: Double?,
    @SerializedName(value = "firstTag")
    val firstTag: String?,
    @SerializedName(value = "secondTag")
    val secondTag: String?,
    @SerializedName(value = "topRecommends")
    val topRecommends: @RawValue JsonObject?,
    @SerializedName(value = "phone")
    val phone: String?,
    @SerializedName(value = "sns")
    val sns: String?,
    @SerializedName(value = "openHours")
    val openHours: String?,
    @SerializedName(value = "price")
    val price: String?,
    @SerializedName(value = "filesPath")
    val filesPath: List<String>?,
    @SerializedName(value = "content")
    val content: String?,
    @SerializedName(value = "rating")
    val rating: Double?,
    @SerializedName(value = "reviewCount")
    val reviewCount: Int?,
    @SerializedName(value = "scrapCount")
    val scrapCount: Int?,
    @SerializedName(value = "createdAt")
    val createdAt: String?,
    @SerializedName(value = "updatedAt")
    val updatedAt: String?,
    @SerializedName(value = "scrapped")
    val scrapped: Boolean?
) : BaseResponse {

    private fun parseTopRecommends(jsonObject: JsonObject): List<Pair<String, Int>>? {
        return jsonObject.entrySet().map {
            Pair(it.key, it.value.asInt)
        }.sortedByDescending { it.second }
    }

    companion object : DataMapper<StudioResponse, Studio> {
        override fun StudioResponse.toDataModel(): Studio {
            return Studio(
                id = id,
                name = name,
                address = address,
                latitude = latitude,
                longitude = longitude,
                firstTag = firstTag,
                secondTag = secondTag,
                topRecommends = topRecommends?.let { parseTopRecommends(it) },
                phone = phone,
                sns = sns,
                openHours = openHours,
                price = price,
                filesPath = filesPath,
                content = content,
                rating = rating,
                reviewCount = reviewCount,
                scrapCount = scrapCount,
                createdAt = createdAt,
                updatedAt = updatedAt,
                scrapped = scrapped
            )
        }
    }
}