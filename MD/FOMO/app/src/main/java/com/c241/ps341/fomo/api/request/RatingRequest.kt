package com.c241.ps341.fomo.api.request

import com.google.gson.annotations.SerializedName

data class RatingRequest(
    @SerializedName("food_id") val foodId: Int,
    @SerializedName("rate") val rate: Int
)