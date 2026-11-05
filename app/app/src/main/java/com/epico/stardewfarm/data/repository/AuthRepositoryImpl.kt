package com.epico.stardewfarm.data.repository

import com.epico.stardewfarm.data.local.SessionStorage
import com.epico.stardewfarm.data.mapper.toDomain
import com.epico.stardewfarm.data.remote.AuthApi
import com.epico.stardewfarm.data.remote.dto.AuthResponseDto
import com.epico.stardewfarm.data.remote.dto.LoginRequest
import com.epico.stardewfarm.data.remote.dto.RegisterRequest
import com.epico.stardewfarm.data.remote.safeApiCall
import com.epico.stardewfarm.domain.model.AppResult
import com.epico.stardewfarm.domain.model.Usuario
import com.epico.stardewfarm.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val session: SessionStorage,
) : AuthRepository {

    override suspend fun registrar(
        nombre: String,
        correo: String,
        contrasena: String,
    ): AppResult<Usuario> = autenticar {
        api.registrar(RegisterRequest(name = nombre, email = correo, password = contrasena))
    }

    override suspend fun iniciarSesion(
        correo: String,
        contrasena: String,
    ): AppResult<Usuario> = autenticar {
        api.iniciarSesion(LoginRequest(email = correo, password = contrasena))
    }

    override fun haySesion(): Boolean = session.obtenerToken() != null

    override fun cerrarSesion() = session.limpiar()

    private suspend fun autenticar(
        call: suspend () -> AuthResponseDto,
    ): AppResult<Usuario> =
        when (val resultado = safeApiCall(call)) {
            is AppResult.Success -> {
                session.guardarToken(resultado.data.accessToken)
                AppResult.Success(resultado.data.user.toDomain())
            }
            is AppResult.Failure -> resultado
        }
}