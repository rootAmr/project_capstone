package com.c241.ps341.fomo.api.response

import com.google.gson.annotations.SerializedName

data class FoodDeleteResponse(
	@field:SerializedName("message")
	val message: String? = null
)
