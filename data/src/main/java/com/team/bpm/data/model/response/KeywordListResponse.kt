package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.KeywordResponse.Companion.toDataModel
import com.team.bpm.domain.model.KeywordList
import kotlinx.parcelize.Parcelize

@Parcelize
data class KeywordListResponse(
    @SerializedName("keywords")
    val keywords: List<KeywordResponse>
) : BaseResponse {
    companion object : DataMapper<KeywordListResponse, KeywordList> {
        override fun KeywordListResponse.toDataModel(): KeywordList {
            return KeywordList(
                keywords = keywords.map { it.toDataModel() }
            )
        }
    }
}

