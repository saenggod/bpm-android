package com.team.bpm.data.model.response

import com.google.gson.annotations.SerializedName
import com.team.bpm.data.base.BaseResponse
import com.team.bpm.data.mapper.DataMapper
import com.team.bpm.data.model.response.BodyShapeDetailResponse.Companion.toDataModel
import com.team.bpm.data.model.response.BodyShapeListResponse.Companion.toDataModel
import com.team.bpm.data.model.response.BodyShapeScheduleResponse.Companion.toDataModel
import com.team.bpm.domain.model.BodyShapeDetail
import com.team.bpm.domain.model.BodyShapeList
import com.team.bpm.domain.model.BodyShapeSchedule
import com.team.bpm.domain.model.BodyShapeSchedules
import kotlinx.parcelize.Parcelize

@Parcelize
data class BodyShapeSchedulesResponse(
    @SerializedName("schedules")
    val schedules: List<BodyShapeScheduleResponse>?,
    @SerializedName("scheduleCount")
    val scheduleCount: Int?
) : BaseResponse {
    companion object : DataMapper<BodyShapeSchedulesResponse, BodyShapeSchedules> {
        override fun BodyShapeSchedulesResponse.toDataModel(): BodyShapeSchedules {
            return BodyShapeSchedules(
                scheduleCount = scheduleCount,
                schedules = schedules?.map { it.toDataModel() }
            )
        }
    }
}

@Parcelize
data class BodyShapeScheduleResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("scheduleName")
    val scheduleName: String?,
    @SerializedName("studioName")
    val studioName: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("memo")
    val memo: String?,
    @SerializedName("createAt")
    val createAt: String?,
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("isTodayPost")
    val isTodayPost: Boolean?,
    @SerializedName("bodyShapeList")
    val bodyShapeList: BodyShapeListResponse?,
    @SerializedName("dday")
    val dday: Int,
) : BaseResponse {
    companion object : DataMapper<BodyShapeScheduleResponse, BodyShapeSchedule> {
        override fun BodyShapeScheduleResponse.toDataModel(): BodyShapeSchedule {
            return BodyShapeSchedule(
                id = id,
                scheduleName = scheduleName,
                studioName = studioName,
                date = date,
                time = time,
                memo = memo,
                createAt = createAt,
                imagePath = imagePath,
                isTodayPost = isTodayPost,
                bodyShapeList = bodyShapeList?.toDataModel(),
                dday = dday
            )
        }
    }
}

@Parcelize
data class BodyShapeListResponse(
    @SerializedName("bodyShapeDetails")
    val bodyShapeDetails: List<BodyShapeDetailResponse>?,
    @SerializedName("bodyShapeListSize")
    val bodyShapeListSize: Int?,
) : BaseResponse {
    companion object : DataMapper<BodyShapeListResponse, BodyShapeList> {
        override fun BodyShapeListResponse.toDataModel(): BodyShapeList {
            return BodyShapeList(
                bodyShapeDetails = bodyShapeDetails?.map { it.toDataModel() } ?: emptyList(),
                bodyShapeListSize = bodyShapeListSize
            )
        }
    }
}

@Parcelize
data class BodyShapeDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("createAt")
    val createAt: String?,
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("dday")
    val dday: Int?,
) : BaseResponse {
    companion object : DataMapper<BodyShapeDetailResponse, BodyShapeDetail> {
        override fun BodyShapeDetailResponse.toDataModel(): BodyShapeDetail {
            return BodyShapeDetail(
                id = id,
                createAt = createAt,
                imagePath = imagePath,
                dday = dday
            )
        }
    }
}