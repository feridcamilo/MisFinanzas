package com.example.data.remote.api

import com.example.data.local.model.BalanceVOBody
import com.example.data.local.model.CategoryVOBody
import com.example.data.remote.model.APIParameterBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @GET("/Servicios/Maestros.svc/getCategorias/{clientId}")
    fun getCategories(@Path("clientId") clientId: String): Call<CategoryVOBody>

    @POST("/Servicios/Movimientos.svc/getSaldos")
    fun getSaldo(@Body parametros: APIParameterBody): Call<BalanceVOBody>
}
