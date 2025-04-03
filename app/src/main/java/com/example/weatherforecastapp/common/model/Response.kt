package com.example.weatherforecastapp.common.model

sealed class Response<out T> {
    object Loading : Response<Nothing>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Failure(val error: Throwable) : Response<Nothing>()
}
