package com.c241.ps341.fomo.api.response

import com.google.gson.annotations.SerializedName

data class CommentPostResponse(

	@field:SerializedName("data")
	val data: CommentPostData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CommentPostData(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("food_id")
	val foodId: Int? = null,

	@field:SerializedName("commentField")
	val commentField: String? = null
)
