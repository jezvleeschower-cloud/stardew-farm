package com.epico.stardewfarm.data.remote

import com.epico.stardewfarm.domain.model.AppResult
import com.epico.stardewfarm.domain.model.DataError
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeApiCall(call: suspend () -> T): AppResult<T> =
    try {
        AppResult.Success(call())
    } catch (e: CancellationException) {
        throw e
    } catch (e: SocketTimeoutException) {
        AppResult.Failure(DataError.Timeout)
    } catch (e: IOException) {
        AppResult.Failure(DataError.NoInternet)
    } catch (e: HttpException) {
        AppResult.Failure(DataError.Http(e.code()))
    } catch (e: SerializationException) {
        AppResult.Failure(DataError.Serialization)
    }