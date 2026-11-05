package com.epico.stardewfarm.data.local

interface SessionStorage {
    fun guardarToken(token: String)
    fun obtenerToken(): String?
    fun limpiar()
}