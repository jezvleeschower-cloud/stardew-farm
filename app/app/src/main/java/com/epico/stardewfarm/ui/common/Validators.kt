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

    fun nombreCultivo(valor: String): String? =
        if (valor.isBlank()) "El nombre es obligatorio" else null

    fun precioVenta(valor: String): String? {
        val n = valor.toIntOrNull() ?: return "Ingresa un precio válido"
        return if (n <= 0) "El precio debe ser mayor a 0" else null
    }

    fun diasCrecimiento(valor: String): String? {
        val n = valor.toIntOrNull() ?: return "Ingresa los días de crecimiento"
        return if (n !in 1..28) "Debe estar entre 1 y 28 días" else null
    }
}