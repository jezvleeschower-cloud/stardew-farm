package com.epico.stardewfarm.data.mapper

import com.epico.stardewfarm.data.remote.dto.UserDto
import com.epico.stardewfarm.model.Usuario

fun UserDto.toDomain(): Usuario = Usuario(
    id = id,
    nombre = name,
    correo = email,
)