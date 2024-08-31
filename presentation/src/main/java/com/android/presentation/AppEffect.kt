package com.android.presentation

sealed class AppEffect {
    data class ShowToast(
        val message: String,
    ) : AppEffect()

    data object NONE : AppEffect()
}
