package com.example.data.remote.api

import com.example.data.remote.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @GET("/Servicios/Maestros.svc/getCategorias/{clientId}")
    fun getCategories(@Path("clientId") clientId: String): Call<CategoryDTO>

    @GET("/Servicios/Maestros.svc/getDeudas/{clientId}")
    fun getDebts(@Path("clientId") clientId: String): Call<DebtDTO>

    @GET("/Servicios/Maestros.svc/getLugares/{clientId}")
    fun getPlaces(@Path("clientId") clientId: String): Call<PlaceDTO>

    @GET("/Servicios/Maestros.svc/getPersonas/{clientId}")
    fun getPeople(@Path("clientId") clientId: String): Call<PersonaDTO>

    @POST("/Servicios/Movimientos.svc/getSaldos")
    fun getSaldo(@Body parametros: APIParameterBody): Call<BalanceDTO>

    @POST("/Servicios/Movimientos.svc/getMovimientos")
    fun getMovements(@Body parametros: APIParameterBody): Call<MovementDTO>
}
