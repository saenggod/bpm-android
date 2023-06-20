package com.team.bpm.domain.repository

import com.team.bpm.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface SplashRepository {

    fun getKakaoId(): Flow<Long?>

    suspend fun setKakaoId(kakaoId: Long): Flow<Long?>

    fun getUserToken(): Flow<String?>

    suspend fun setUserToken(userToken: String): Flow<String?>

    suspend fun sendSignIn(kakaoId: Long): Flow<UserProfile>
}