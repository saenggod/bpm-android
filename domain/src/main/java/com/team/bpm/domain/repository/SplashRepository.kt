package com.team.bpm.domain.repository

import com.team.bpm.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface SplashRepository {

    fun getKakaoId(): Flow<Long?>

    suspend fun setKakaoId(kakaoId: Long): Flow<Long?>

    fun getUserToken(): String

    fun setUserToken(userToken: String)

    suspend fun sendSignIn(kakaoId: Long): Flow<UserProfile>
}