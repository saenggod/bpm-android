package com.team.bpm.domain.model.request_wrapper

data class RegisterStudioWrapper(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val recommends: List<String>,
    val phone: String,
    val sns: String,
    val openHours: String,
    val price: String
)