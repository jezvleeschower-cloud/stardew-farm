package com.epico.stardewfarm.domain.model

enum class Estacion(val etiqueta: String) {
    PRIMAVERA("Primavera"),
    VERANO("Verano"),
    OTONO("Otoño"),
    INVIERNO("Invierno");

    companion object {
        fun desdeEtiqueta(valor: String): Estacion =
            entries.firstOrNull { it.etiqueta == valor } ?: PRIMAVERA
    }
}