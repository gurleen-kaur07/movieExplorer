package com.android.domain.util


import java.net.SocketTimeoutException

object SafeCall {
    suspend inline fun <T> safeCall(crossinline apiCall: suspend () -> T): Resource<T> =
        try {
            val response = apiCall()
            Resource.Success(response)
        } catch (e: SocketTimeoutException) {
            Resource.Error(AppException.SOCKET_TIME_OUT.message)
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
}
