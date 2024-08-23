package com.android.presentation

sealed class AppEffect {
    data class ShowToast(
        val message: String,
    ) : AppEffect()

    object NONE : AppEffect()

    data class NetworkError(
        val message: String,
    ) : AppEffect()
}
