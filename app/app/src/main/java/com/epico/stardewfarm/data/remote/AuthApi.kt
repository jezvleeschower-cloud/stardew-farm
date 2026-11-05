package com.epico.stardewfarm.data.remote

import com.epico.stardewfarm.data.remote.dto.AuthResponseDto
import com.epico.stardewfarm.data.remote.dto.LoginRequest
import com.epico.stardewfarm.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("register")
    suspend fun registrar(@Body body: RegisterRequest): AuthResponseDto

    @POST("login")
    suspend fun iniciarSesion(@Body body: LoginRequest): AuthResponseDto
}