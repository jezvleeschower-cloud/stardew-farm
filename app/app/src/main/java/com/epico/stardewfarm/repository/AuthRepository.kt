package com.epico.stardewfarm.repository

import com.epico.stardewfarm.model.AppResult
import com.epico.stardewfarm.model.Usuario

interface AuthRepository {
    suspend fun registrar(nombre: String, correo: String, contrasena: String): AppResult<Usuario>
    suspend fun iniciarSesion(correo: String, contrasena: String): AppResult<Usuario>
    fun haySesion(): Boolean
    fun cerrarSesion()
}