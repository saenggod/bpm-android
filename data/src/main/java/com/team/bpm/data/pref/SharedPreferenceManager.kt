package com.team.bpm.data.pref

import android.content.Context

class SharedPreferenceManager(context: Context) {

    private val sharedPreference =
        context.getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE)

    private fun get(key: String): String? {
        return sharedPreference.getString(key, null)
    }

    private fun set(key: String, value: String?) {
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getToken(): String {
        return get(KEY_TOKEN) ?: ""
    }

    fun setToken(token : String) {
        set(KEY_TOKEN, token)
    }

    companion object {
        private const val KEY_PREF = "pref_bpm"

        private const val KEY_TOKEN = "token"
    }
}