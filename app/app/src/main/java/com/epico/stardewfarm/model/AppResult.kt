package com.epico.stardewfarm.model

sealed interface DataError {
    data object NoInternet : DataError
    data object Timeout : DataError
    data class Http(val code: Int) : DataError
    data object Serialization : DataError
    data object NoSession : DataError
    data object Unknown : DataError
}

sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>
    data class Failure(val error: DataError) : AppResult<Nothing>
}