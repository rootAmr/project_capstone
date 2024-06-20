package com.c241.ps341.fomo.api.response

import com.google.gson.annotations.SerializedName

data class FoodPatchResponse(

	@field:SerializedName("data")
	val data: FoodPhotoData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class FoodPhotoData(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
