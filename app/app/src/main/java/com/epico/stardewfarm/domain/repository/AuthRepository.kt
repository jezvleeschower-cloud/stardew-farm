package com.epico.stardewfarm.domain.repository

import com.epico.stardewfarm.domain.model.AppResult
import com.epico.stardewfarm.domain.model.Usuario

interface AuthRepository {
    suspend fun registrar(nombre: String, correo: String, contrasena: String): AppResult<Usuario>
    suspend fun iniciarSesion(correo: String, contrasena: String): AppResult<Usuario>
    fun haySesion(): Boolean
    fun cerrarSesion()
}