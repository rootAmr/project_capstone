package com.c241.ps341.fomo.api.request

import com.google.gson.annotations.SerializedName

data class SearchRequest(
    @SerializedName("query") val query: String
)