package com.epico.stardewfarm.model

data class Cultivo(
    val id: Int = 0,
    val nombre: String,
    val estacion: Estacion,
    val precioVenta: Int,
    val diasCrecimiento: Int,
    val seRecultiva: Boolean,
)