package com.epico.stardewfarm.domain.repository

import com.epico.stardewfarm.domain.model.AppResult
import com.epico.stardewfarm.domain.model.Cultivo

interface CultivoRepository {
    suspend fun listar(): AppResult<List<Cultivo>>
    suspend fun obtener(id: Int): AppResult<Cultivo>
    suspend fun crear(cultivo: Cultivo): AppResult<Cultivo>
    suspend fun actualizar(cultivo: Cultivo): AppResult<Cultivo>
    suspend fun eliminar(id: Int): AppResult<Unit>
}