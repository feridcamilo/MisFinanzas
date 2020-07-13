package com.android.data.remote.api

import com.android.data.remote.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @GET("Maestros.svc/getCategorias/{clientId}")
    suspend fun getCategories(@Path("clientId") clientId: String): CategoryDTO

    @GET("Maestros.svc/getDeudas/{clientId}")
    suspend fun getDebts(@Path("clientId") clientId: String): DebtDTO

    @GET("Maestros.svc/getLugares/{clientId}")
    suspend fun getPlaces(@Path("clientId") clientId: String): PlaceDTO

    @GET("Maestros.svc/getPersonas/{clientId}")
    suspend fun getPeople(@Path("clientId") clientId: String): PersonaDTO

    @GET("Maestros.svc/getUsuario/{user}/{password}")
    suspend fun getUser(@Path("user") user: String, @Path("password") password: String): UserDTO

    @POST("Movimientos.svc/getSaldos")
    suspend fun getSaldo(@Body parametros: APIParameterBody): BalanceDTO

    @POST("Movimientos.svc/getMovimientos")
    suspend fun getMovements(@Body parametros: APIParameterBody): MovementDTO
}
