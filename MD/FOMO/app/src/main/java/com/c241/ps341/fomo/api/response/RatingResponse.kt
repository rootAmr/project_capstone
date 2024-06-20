package com.c241.ps341.fomo.api.response

import com.google.gson.annotations.SerializedName

data class RatingResponse(

	@field:SerializedName("data")
	val data: List<RatingDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class RatingDataItem(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("rate")
	val rate: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("food_id")
	val foodId: Int? = null
)
