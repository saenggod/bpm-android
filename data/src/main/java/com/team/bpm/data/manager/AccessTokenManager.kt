package com.team.bpm.data.manager

import com.team.bpm.data.BuildConfig
import com.team.bpm.data.pref.SharedPreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AccessTokenManager @Inject constructor(
    private val sharedPreferenceManager: SharedPreferenceManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val shouldBeAuthorized = request.header(KEY_SHOULD_BE_AUTHORIZED) != "false"

        val requestBuilder = request.newBuilder()

        val token = sharedPreferenceManager.getToken()

//        if (BuildConfig.DEBUG) {
//            requestBuilder.addHeader(KEY_HEADER, TOKEN_TEST)
//        } else {
//            requestBuilder.addHeader(KEY_HEADER, token)
//        }

        requestBuilder.addHeader(KEY_HEADER, token)

        if (!shouldBeAuthorized) {
            requestBuilder.removeHeader("shouldBeAuthorized")
        }

        return chain.proceed(requestBuilder.build())
    }

    companion object {
        const val KEY_HEADER = "Authorization"
        const val KEY_SHOULD_BE_AUTHORIZED = "shouldBeAuthorized"

        const val TOKEN_TEST =
            "Token eyJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjoiNiIsImlhdCI6MTY4NTA3MDQ0NCwiZXhwIjoxNjg4MDcwNDQ0fQ.QhzaeQj8kCLOOaMawbUNXKv849g4M9QItIfKZfSEcio" // forTest
    }
}