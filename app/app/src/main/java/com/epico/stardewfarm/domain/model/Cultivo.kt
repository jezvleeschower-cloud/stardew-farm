package com.epico.stardewfarm.domain.model

data class Cultivo(
    val id: Int = 0,
    val nombre: String,
    val estacion: Estacion,
    val precioVenta: Int,
    val diasCrecimiento: Int,
    val seRecultiva: Boolean,
)