package com.c241.ps341.fomo.api.response

import com.google.gson.annotations.SerializedName

data class RatingPostResponse(

	@field:SerializedName("data")
	val data: RatingPostData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class RatingPostData(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("rate")
	val rate: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("food_id")
	val foodId: Int? = null
)
