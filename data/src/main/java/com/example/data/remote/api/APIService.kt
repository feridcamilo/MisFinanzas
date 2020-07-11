package com.example.data.remote.api

import com.example.data.remote.model.APIParameterBody
import com.example.data.remote.model.BalanceDTO
import com.example.data.remote.model.CategoryDTO
import com.example.data.remote.model.MovementDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @GET("/Servicios/Maestros.svc/getCategorias/{clientId}")
    fun getCategories(@Path("clientId") clientId: String): Call<CategoryDTO>

    @POST("/Servicios/Movimientos.svc/getSaldos")
    fun getSaldo(@Body parametros: APIParameterBody): Call<BalanceDTO>

    @POST("/Servicios/Movimientos.svc/getMovimientos")
    fun getMovimientos(@Body parametros: APIParameterBody): Call<MovementDTO>
}
