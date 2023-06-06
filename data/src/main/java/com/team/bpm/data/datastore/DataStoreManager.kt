package com.team.bpm.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val context: Context) {
    private val Context.instance: DataStore<Preferences> by preferencesDataStore(KEY_DATASTORE)

    fun getKakaoId(): Flow<Long?> {
        return context.instance.data.map { preferences ->
            preferences[longPreferencesKey(KEY_KAKAO_USER_ID)]
        }
    }

    suspend fun setKakaoId(kakaoId: Long): Flow<Long?> {
        context.instance.edit { preferences ->
            preferences[longPreferencesKey(KEY_KAKAO_USER_ID)] = kakaoId
        }

        return getKakaoId()
    }

    fun getUserToken(): Flow<String?> {
        return context.instance.data.map { preferences ->
            preferences[stringPreferencesKey(KEY_USER_TOKEN)]
        }
    }

    suspend fun setUserToken(token: String): Flow<String?> {
        context.instance.edit { preferences ->
            preferences[stringPreferencesKey(KEY_USER_TOKEN)] = token
        }

        return getUserToken()
    }

    fun getStartTabIndex(): Flow<Int?> {
        return context.instance.data.map { preferences ->
            preferences[intPreferencesKey(KEY_START_TAB)]
        }
    }

    suspend fun setStartTabIndex(index: Int): Flow<Int?> {
        context.instance.edit { preferences ->
            preferences[intPreferencesKey(KEY_START_TAB)] = index
        }

        return getStartTabIndex()
    }

    companion object {
        private const val KEY_DATASTORE = "bpm"
        private const val KEY_KAKAO_USER_ID = "kakao_user_id"
        private const val KEY_USER_TOKEN = "user_token"
        private const val KEY_START_TAB = "start_tab"
    }
}