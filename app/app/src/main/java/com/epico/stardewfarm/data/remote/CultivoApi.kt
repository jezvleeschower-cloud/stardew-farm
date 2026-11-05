package com.epico.stardewfarm.data.remote

import com.epico.stardewfarm.data.remote.dto.CultivoDto
import com.epico.stardewfarm.data.remote.dto.CultivoRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CultivoApi {
    @GET("cultivos")
    suspend fun listar(): List<CultivoDto>

    @GET("cultivos/{id}")
    suspend fun obtener(@Path("id") id: Int): CultivoDto

    @POST("cultivos")
    suspend fun crear(@Body body: CultivoRequest): CultivoDto

    @PUT("cultivos/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body body: CultivoRequest): CultivoDto

    @DELETE("cultivos/{id}")
    suspend fun eliminar(@Path("id") id: Int)
}