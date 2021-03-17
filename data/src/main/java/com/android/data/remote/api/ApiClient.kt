package com.android.data.remote.api

import com.android.data.BuildConfig
import com.android.domain.AppConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val CONTENT_TYPE_JSON = "application/json"
    private const val READ_TIMEOUT = 1L
    private const val CONNECT_TIMEOUT = 1L

    @ExperimentalSerializationApi
    val service: APIService by lazy {
        val httpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES)
            .build()

        val contentType = CONTENT_TYPE_JSON.toMediaType()
        Retrofit.Builder()
            .baseUrl(AppConfig.BASE_SERVICES_URL)
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build().create(APIService::class.java)
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        allowSpecialFloatingPointValues = true
        useArrayPolymorphism = true
    }
}
