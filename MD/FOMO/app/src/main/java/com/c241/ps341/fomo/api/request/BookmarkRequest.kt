package com.c241.ps341.fomo.api.request

import com.google.gson.annotations.SerializedName

data class BookmarkRequest(
    @SerializedName("food_id") val foodId: Int
)