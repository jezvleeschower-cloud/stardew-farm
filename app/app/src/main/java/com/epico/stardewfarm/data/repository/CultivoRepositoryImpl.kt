package com.epico.stardewfarm.data.repository

import com.epico.stardewfarm.data.mapper.toDomain
import com.epico.stardewfarm.data.mapper.toRequest
import com.epico.stardewfarm.data.remote.CultivoApi
import com.epico.stardewfarm.data.remote.safeApiCall
import com.epico.stardewfarm.model.AppResult
import com.epico.stardewfarm.model.Cultivo
import com.epico.stardewfarm.repository.CultivoRepository

class CultivoRepositoryImpl(
    private val api: CultivoApi,
) : CultivoRepository {

    override suspend fun listar(): AppResult<List<Cultivo>> =
        safeApiCall { api.listar().map { it.toDomain() } }

    override suspend fun obtener(id: Int): AppResult<Cultivo> =
        safeApiCall { api.obtener(id).toDomain() }

    override suspend fun crear(cultivo: Cultivo): AppResult<Cultivo> =
        safeApiCall { api.crear(cultivo.toRequest()).toDomain() }

    override suspend fun actualizar(cultivo: Cultivo): AppResult<Cultivo> =
        safeApiCall { api.actualizar(cultivo.id, cultivo.toRequest()).toDomain() }

    override suspend fun eliminar(id: Int): AppResult<Unit> =
        safeApiCall { api.eliminar(id) }
}