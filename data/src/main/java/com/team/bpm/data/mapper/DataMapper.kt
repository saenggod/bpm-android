package com.team.bpm.data.mapper

import com.team.bpm.data.base.BaseResponse
import com.team.bpm.domain.base.BaseModel

interface DataMapper<in R : BaseResponse, out D : BaseModel> {
    fun R.toDataModel(): D
}
