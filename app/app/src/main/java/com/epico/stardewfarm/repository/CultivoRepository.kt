package com.epico.stardewfarm.repository

import com.epico.stardewfarm.model.AppResult
import com.epico.stardewfarm.model.Cultivo

interface CultivoRepository {
    suspend fun listar(): AppResult<List<Cultivo>>
    suspend fun obtener(id: Int): AppResult<Cultivo>
    suspend fun crear(cultivo: Cultivo): AppResult<Cultivo>
    suspend fun actualizar(cultivo: Cultivo): AppResult<Cultivo>
    suspend fun eliminar(id: Int): AppResult<Unit>
}