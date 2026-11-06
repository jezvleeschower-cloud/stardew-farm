package com.epico.stardewfarm.data.mapper

import com.epico.stardewfarm.data.remote.dto.CultivoDto
import com.epico.stardewfarm.data.remote.dto.CultivoRequest
import com.epico.stardewfarm.model.Cultivo
import com.epico.stardewfarm.model.Estacion

fun CultivoDto.toDomain(): Cultivo = Cultivo(
    id = id,
    nombre = nombre,
    estacion = Estacion.desdeEtiqueta(estacion),
    precioVenta = precioVenta,
    diasCrecimiento = diasCrecimiento,
    seRecultiva = seRecultiva,
)

fun Cultivo.toRequest(): CultivoRequest = CultivoRequest(
    nombre = nombre,
    estacion = estacion.etiqueta,
    precioVenta = precioVenta,
    diasCrecimiento = diasCrecimiento,
    seRecultiva = seRecultiva,
)