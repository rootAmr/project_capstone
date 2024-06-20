package com.c241.ps341.fomo.api.response

import com.google.gson.annotations.SerializedName

data class BookmarkPostResponse(

	@field:SerializedName("data")
	val data: FoodData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class FoodData(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("food_id")
	val foodId: Int? = null
)
