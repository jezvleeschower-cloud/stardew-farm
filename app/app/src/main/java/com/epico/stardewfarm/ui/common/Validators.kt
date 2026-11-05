package com.epico.stardewfarm.ui.common

object Validators {

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun nombre(valor: String): String? =
        if (valor.isBlank()) "El nombre es obligatorio" else null

    fun correo(valor: String): String? = when {
        valor.isBlank() -> "El correo es obligatorio"
        !EMAIL_REGEX.matches(valor.trim()) -> "El formato del correo no es válido"
        else -> null
    }

    fun contrasena(valor: String): String? = when {
        valor.isEmpty() -> "La contraseña es obligatoria"
        valor.length < 8 -> "Debe tener al menos 8 caracteres"
        else -> null
    }

    fun confirmacion(contrasena: String, confirmacion: String): String? = when {
        confirmacion.isEmpty() -> "Confirma tu contraseña"
        contrasena != confirmacion -> "Las contraseñas no coinciden"
        else -> null
    }
}