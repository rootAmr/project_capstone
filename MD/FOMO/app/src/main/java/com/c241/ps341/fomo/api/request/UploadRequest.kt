package com.c241.ps341.fomo.api.request

import com.google.gson.annotations.SerializedName

data class UploadRequest(
    @SerializedName("foodName") val foodName: String,
    @SerializedName("ingredients") val ingredients: String,
    @SerializedName("steps") val steps: String,
    @SerializedName("category") val category: String
)