package com.c241.ps341.fomo.api.request

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("food_id") val foodId: Int,
    @SerializedName("commentField") val commentField: String
)
