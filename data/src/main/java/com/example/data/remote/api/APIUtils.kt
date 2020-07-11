package com.example.data.remote.api

import com.example.data.remote.RetrofitClient

class APIUtils {
    companion object {
        val BASE_URL = "http://misfinanzas.somee.com/"

        fun getAPIService(): APIService {
            return RetrofitClient.getClient(BASE_URL).create(APIService::class.java)
        }
    }
}
