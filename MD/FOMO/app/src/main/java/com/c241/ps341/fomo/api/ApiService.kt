package com.c241.ps341.fomo.api

import com.c241.ps341.fomo.api.request.BookmarkRequest
import com.c241.ps341.fomo.api.request.CommentRequest
import com.c241.ps341.fomo.api.request.RatingRequest
import com.c241.ps341.fomo.api.request.SearchRequest
import com.c241.ps341.fomo.api.request.UploadRequest
import com.c241.ps341.fomo.api.response.BookmarkPostResponse
import com.c241.ps341.fomo.api.response.BookmarkResponse
import com.c241.ps341.fomo.api.response.CommentPostResponse
import com.c241.ps341.fomo.api.response.CommentResponse
import com.c241.ps341.fomo.api.response.FoodDeleteResponse
import com.c241.ps341.fomo.api.response.FoodPatchResponse
import com.c241.ps341.fomo.api.response.FoodPostResponse
import com.c241.ps341.fomo.api.response.FoodResponse
import com.c241.ps341.fomo.api.response.RatingPostData
import com.c241.ps341.fomo.api.response.RatingPostResponse
import com.c241.ps341.fomo.api.response.RatingResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("foods")
    suspend fun getFoods(@Header("Authorization") token: String): FoodResponse

    @POST("foods")
    suspend fun postFood(@Header("Authorization") token: String, @Body request: UploadRequest): FoodPostResponse

    @Multipart
    @PATCH("foods/{id}")
    suspend fun patchFood(@Header("Authorization") token: String, @Path("id") id: Int, @Part image: MultipartBody.Part): FoodPatchResponse

    @DELETE("foods/{id}")
    suspend fun deleteFood(@Header("Authorization") token: String, @Path("id") id: Int): FoodDeleteResponse

    @GET("bookmarks")
    suspend fun getBookmarks(@Header("Authorization") token: String): BookmarkResponse

    @POST("bookmarks")
    suspend fun postBookmark(@Header("Authorization") token: String, @Body request: BookmarkRequest): BookmarkPostResponse

    @DELETE("bookmarks/{id}")
    suspend fun deleteBookmark(@Header("Authorization") token: String, @Path("id") id: Int): BookmarkResponse

    @GET("comments")
    suspend fun getComments(@Header("Authorization") token: String): CommentResponse

    @POST("comments")
    suspend fun postComment(@Header("Authorization") token: String, @Body request: CommentRequest): CommentPostResponse

    @GET("ratings")
    suspend fun getRatings(@Header("Authorization") token: String): RatingResponse

    @POST("ratings")
    suspend fun postRating(@Header("Authorization") token: String, @Body request: RatingRequest): RatingPostResponse

    @POST("foods/search")
    suspend fun postSearch(@Header("Authorization") token: String, @Body request: SearchRequest): FoodResponse
}