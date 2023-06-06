package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.QuestionResponse.Companion.toDataModel
import com.team.bpm.domain.model.QuestionList
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionListResponse(
    @SerializedName("questionBoardResponseList")
    val questionBoardResponseList: List<QuestionResponse>?,
    @SerializedName("questionBoardCount")
    val questionBoardCount: Int?
) : BaseResponse {
    companion object : DataMapper<QuestionListResponse, QuestionList> {
        override fun QuestionListResponse.toDataModel(): QuestionList {
            return QuestionList(
                questionBoardList = questionBoardResponseList?.map { it.toDataModel() },
                questionBoardCount = questionBoardCount
            )
        }
    }
}