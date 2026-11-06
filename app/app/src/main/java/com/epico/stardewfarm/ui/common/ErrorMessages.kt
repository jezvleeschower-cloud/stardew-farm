package com.epico.stardewfarm.ui.common

import com.epico.stardewfarm.model.DataError

fun DataError.toMessage(http400: String = "Los datos enviados no son válidos"): String =
    when (this) {
        DataError.NoInternet -> "Sin conexión. Revisa tu internet o que la API esté encendida."
        DataError.Timeout -> "El servidor tardó demasiado en responder."
        is DataError.Http -> when (code) {
            400 -> http400
            401, 403 -> "Tu sesión expiró. Inicia sesión de nuevo."
            404 -> "No se encontró el recurso."
            in 500..599 -> "Error del servidor. Intenta más tarde."
            else -> "Error inesperado del servidor ($code)."
        }
        DataError.Serialization -> "La respuesta del servidor no tiene el formato esperado."
        DataError.NoSession -> "No hay una sesión activa."
        DataError.Unknown -> "Ocurrió un error inesperado."
    }