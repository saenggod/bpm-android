package com.team.bpm.data.model.request

import com.google.gson.annotations.SerializedName

data class StudioRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("recommends")
    val recommends: List<String>,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("sns")
    val sns: String,
    @SerializedName("openHours")
    val openHours: String,
    @SerializedName("price")
    val price: String
)
