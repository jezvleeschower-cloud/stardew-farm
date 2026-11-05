package com.epico.stardewfarm.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CultivoDto(
    val id: Int,
    val nombre: String,
    val estacion: String,
    @SerialName("precio_venta") val precioVenta: Int,
    @SerialName("dias_crecimiento") val diasCrecimiento: Int,
    @SerialName("se_recultiva") val seRecultiva: Boolean,
)