package com.android.domain.util

object SafeCall {
    suspend inline fun <T> safeCall(crossinline apiCall: suspend () -> T): Resource<T> =
        try {
            val response = apiCall()
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
}
