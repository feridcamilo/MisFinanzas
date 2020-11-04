package com.android.data.remote.api

import com.android.data.remote.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @GET("Maestros.svc/getCategorias/{clientId}/{lastsync}")
    suspend fun getCategories(@Path("clientId") clientId: String, @Path("lastsync") lastSync: String): CategoryDTO

    @GET("Maestros.svc/getDeudas/{clientId}/{lastsync}")
    suspend fun getDebts(@Path("clientId") clientId: String, @Path("lastsync") lastSync: String): DebtDTO

    @GET("Maestros.svc/getLugares/{clientId}/{lastsync}")
    suspend fun getPlaces(@Path("clientId") clientId: String, @Path("lastsync") lastSync: String): PlaceDTO

    @GET("Maestros.svc/getPersonas/{clientId}/{lastsync}")
    suspend fun getPeople(@Path("clientId") clientId: String, @Path("lastsync") lastSync: String): PersonaDTO

    @POST("Maestros.svc/recibirMaestros")
    suspend fun sendMasters(@Body sendMasters: SendMaster): SendMastersDTO

    @GET("Maestros.svc/getUsuario/{user}/{password}")
    suspend fun getUser(@Path("user") user: String, @Path("password") password: String): UserDTO

    @POST("Movimientos.svc/getSaldos")
    suspend fun getSaldo(@Body parametros: APIParameterBody): BalanceDTO

    @POST("Movimientos.svc/getMovimientos")
    suspend fun getMovements(@Body parametros: APIParameterBody): MovementDTO

    @POST("Movimientos.svc/getMovimientosEliminados")
    suspend fun getDeletedMovements(@Body parametros: APIParameterBody): DeletedMovementDTO

    @POST("Movimientos.svc/eliminarMovimientos")
    suspend fun deleteMovements(@Body deletedMovement: DeletedMovement): DeletedMovementDTO

    @POST("Movimientos.svc/recibirMovimientos")
    suspend fun sendMovements(@Body sendMovement: SendMovement): SendMovementDTO

    @GET("Movimientos.svc/getServerDateTime")
    suspend fun getServerDateTime(): ServerDateTime
}
