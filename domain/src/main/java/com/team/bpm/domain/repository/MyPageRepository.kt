package com.team.bpm.domain.repository

import com.team.bpm.domain.model.ResponseState
import com.team.bpm.domain.model.StudioList
import com.team.bpm.domain.model.UserSchedule
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {

    fun getMainTabIndex(): Flow<Int?>

    suspend fun setMainTabIndex(index : Int): Flow<Int?>

}