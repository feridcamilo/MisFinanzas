package com.android.data.remote.api

import com.android.data.remote.model.APIParameterBody
import com.android.data.remote.model.DeletedMovementDTO
import com.android.data.remote.model.SendMasterDTO
import com.android.data.remote.model.SendMovementDTO
import com.android.data.remote.model.result.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @GET("Maestros.svc/getCategorias/{clientId}/{lastsync}")
    suspend fun getCategories(@Path("clientId") clientId: String, @Path("lastsync") lastSync: String): CategoryResult

    @GET("Maestros.svc/getDeudas/{clientId}/{lastsync}")
    suspend fun getDebts(@Path("clientId") clientId: String, @Path("lastsync") lastSync: String): DebtResult

    @GET("Maestros.svc/getLugares/{clientId}/{lastsync}")
    suspend fun getPlaces(@Path("clientId") clientId: String, @Path("lastsync") lastSync: String): PlaceResult

    @GET("Maestros.svc/getPersonas/{clientId}/{lastsync}")
    suspend fun getPeople(@Path("clientId") clientId: String, @Path("lastsync") lastSync: String): PersonResult

    @POST("Maestros.svc/recibirMaestros")
    suspend fun sendMasters(@Body sendMasters: SendMasterDTO): SendMastersResult

    @GET("Maestros.svc/getUsuario/{user}/{password}")
    suspend fun getUser(@Path("user") user: String, @Path("password") password: String): UserResult

    @POST("Movimientos.svc/getSaldos")
    suspend fun getSaldo(@Body parametros: APIParameterBody): BalanceResult

    @POST("Movimientos.svc/getMovimientos")
    suspend fun getMovements(@Body parametros: APIParameterBody): MovementResult

    @POST("Movimientos.svc/getMovimientosEliminados")
    suspend fun getDeletedMovements(@Body parametros: APIParameterBody): DeletedMovementResult

    @POST("Movimientos.svc/eliminarMovimientos")
    suspend fun deleteMovements(@Body deletedMovement: DeletedMovementDTO): DeletedMovementResult

    @POST("Movimientos.svc/recibirMovimientos")
    suspend fun sendMovements(@Body sendMovement: SendMovementDTO): SendMovementResult

    @GET("Movimientos.svc/getServerDateTime")
    suspend fun getServerDateTime(): ServerDateTimeResult
}
