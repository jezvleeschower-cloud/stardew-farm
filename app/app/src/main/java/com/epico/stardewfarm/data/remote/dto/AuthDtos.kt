package com.epico.stardewfarm.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class UserDto(
    val id: Int,
    val email: String,
    val name: String = "",
)

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val user: UserDto,
)