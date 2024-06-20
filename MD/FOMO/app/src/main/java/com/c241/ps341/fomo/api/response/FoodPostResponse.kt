package com.c241.ps341.fomo.api.response

import com.google.gson.annotations.SerializedName

data class FoodPostResponse(

	@field:SerializedName("data")
	val data: FoodPostData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class FoodPostData(

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("foodName")
	val foodName: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("rating")
	val rating: Float? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("steps")
	val steps: String? = null,

	@field:SerializedName("url")
	val url: Any? = null
)
