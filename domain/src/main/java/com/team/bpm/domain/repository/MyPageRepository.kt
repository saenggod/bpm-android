package com.team.bpm.domain.repository

import kotlinx.coroutines.flow.Flow

interface MyPageRepository {

    fun getMainTabIndex(): Flow<Int?>

    suspend fun setMainTabIndex(index: Int): Flow<Int?>

}