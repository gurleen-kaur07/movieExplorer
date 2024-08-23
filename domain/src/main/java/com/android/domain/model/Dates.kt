package com.android.domain.model

import com.google.gson.annotations.SerializedName

data class Dates(
    @field:SerializedName("maximum")
    val maximum: String,
    @field:SerializedName("minimum")
    val minimum: String,
)
