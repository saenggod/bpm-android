package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.domain.model.Keyword
import kotlinx.parcelize.Parcelize

@Parcelize
data class KeywordResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("keyword")
    val keyword: String?
) : BaseResponse {
    companion object : DataMapper<KeywordResponse, Keyword> {
        override fun KeywordResponse.toDataModel(): Keyword {
            return Keyword(
                id = id,
                keyword = keyword
            )
        }
    }
}
