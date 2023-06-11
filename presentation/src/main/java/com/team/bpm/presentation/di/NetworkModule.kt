package com.team.bpm.presentation.di

import com.team.bpm.data.network.MainApi
import com.team.bpm.presentation.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val TOKEN_TEST = "Token eyJhbGciOiJIUzI1NiJ9.eyJ1dWlkIjoiNiIsImlhdCI6MTY4NjE0ODAyMSwiZXhwIjoxNjg5MTQ4MDIxfQ.qZmBC3OyHR4yVYbGFzDXzq8uwC3Zz71aeoA1rLkYCCo" // forTest

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
    fun provideNullOnEmptyConverterFactory(): Converter.Factory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
            override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) {
                try {
                    nextResponseBodyConverter.convert(value)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            } else {
                null
            }
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        nullOnEmptyConverterFactory: Converter.Factory,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit): MainApi {
        return retrofit.create(MainApi::class.java)
    }
}