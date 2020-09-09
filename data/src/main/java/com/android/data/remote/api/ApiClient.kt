package com.android.data.remote.api

import com.android.data.AppConfig
import com.android.data.remote.model.converter.JsonDateDeserializer
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object ApiClient {
    val service: APIService by lazy {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logInterceptor)

        val gsonBuilder = GsonBuilder().registerTypeAdapter(Date::class.java, JsonDateDeserializer()).create()

        Retrofit.Builder()
            .baseUrl(AppConfig.BASE_SERVICES_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build().create(APIService::class.java)
    }
}
