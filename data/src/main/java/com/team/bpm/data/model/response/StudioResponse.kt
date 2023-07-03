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
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("addressDescription")
    val addressDescription: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("firstTag")
    val firstTag: String?,
    @SerializedName("secondTag")
    val secondTag: String?,
    @SerializedName("topRecommends")
    val topRecommends: @RawValue JsonObject?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("sns")
    val sns: String?,
    @SerializedName("openHours")
    val openHours: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("filesPath")
    val filesPath: List<String>?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("reviewCount")
    val reviewCount: Int?,
    @SerializedName("scrapCount")
    val scrapCount: Int?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("scrapped")
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
                addressDescription = addressDescription?.replace("\\n", "\n"),
                latitude = latitude,
                longitude = longitude,
                firstTag = firstTag,
                secondTag = secondTag,
                topRecommends = topRecommends?.let { parseTopRecommends(it) },
                phone = phone?.replace("\\n", "\n"),
                sns = sns?.replace("\\n", "\n"),
                openHours = openHours?.replace("\\n", "\n"),
                price = price?.replace("\\n", "\n"),
                filesPath = filesPath,
                content = content?.replace("\\n", "\n"),
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