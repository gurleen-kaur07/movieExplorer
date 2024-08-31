package com.android.domain.util

enum class AppException(
    val message: String,
){
    SOCKET_TIME_OUT("Access to The Movie Database (TMDB) has been suspended for some IP addresses." + "\n Please try again or try switching to a different network.")
}