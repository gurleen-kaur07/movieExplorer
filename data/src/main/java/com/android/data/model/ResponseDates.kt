package com.android.data.model

import com.google.gson.annotations.SerializedName

data class ResponseDates(
    @field:SerializedName("maximum")
    val maximum: String,
    @field:SerializedName("minimum")
    val minimum: String,
)
