package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.Album
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("albumName")
    val albumName: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("memo")
    val memo: String?,
) : BaseResponse {
    companion object : DataMapper<AlbumResponse, Album> {
        override fun AlbumResponse.toDataModel(): Album {
            return Album(
                id = id,
                albumName = albumName,
                date = date,
                memo = memo
            )
        }
    }
}