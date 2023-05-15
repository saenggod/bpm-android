package com.team.bpm.presentation.di

import com.team.bpm.presentation.BuildConfig
import com.team.bpm.data.network.MainApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val TOKEN_TEST = "Token eyJhbGciOiJIUzI1NiJ9.eyJuaWNrbmFtZSI6Ilwi7KCA66as7KCA66as7KCA66asXCIiLCJpYXQiOjE2ODQwNzI0NjYsImV4cCI6MTY4NzA3MjQ2Nn0.Ak6H5fAhz5Ntu6uazSZ2oXh1AqueDK56N-eUwD6PaI4" // forTest

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val BASE_URL = "http://3.36.240.13/"

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val client = OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
//            client.addNetworkInterceptor(
//                FlipperOkhttpInterceptor(
//                    AndroidFlipperClient.getInstance(App().applicationContext).getPlugin(
//                        NetworkFlipperPlugin.ID
//                    )
//                )
//            )
            client.addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            })

            client.addInterceptor(Interceptor { chain ->
                val request = chain.request()
                val shouldBeAuthorized = request.header(name = "shouldBeAuthorized") != "false"
                val requestBuilder = request.newBuilder()

                if (shouldBeAuthorized) {
                    requestBuilder.addHeader(
                        name = "Authorization",
                        value = TOKEN_TEST
                    )

                } else {
                    requestBuilder.removeHeader(name = "shouldBeAuthorized")
                }

                return@Interceptor chain.proceed(request = requestBuilder.build())
            })

        } else {
            client.addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.NONE
            })
        }

        return client.build()
    }


    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit): MainApi {
        return retrofit.create(MainApi::class.java)
    }
}