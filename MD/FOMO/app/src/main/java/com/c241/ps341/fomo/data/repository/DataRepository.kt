package com.c241.ps341.fomo.data.repository

import com.c241.ps341.fomo.api.ApiService
import com.c241.ps341.fomo.api.request.BookmarkRequest
import com.c241.ps341.fomo.api.request.CommentRequest
import com.c241.ps341.fomo.api.request.RatingRequest
import com.c241.ps341.fomo.api.request.SearchRequest
import com.c241.ps341.fomo.api.request.UploadRequest
import com.c241.ps341.fomo.api.response.BookmarkDataItem
import com.c241.ps341.fomo.api.response.CommentDataItem
import com.c241.ps341.fomo.api.response.FoodDataItem
import com.c241.ps341.fomo.api.response.FoodPostData
import com.c241.ps341.fomo.api.response.RatingDataItem
import com.c241.ps341.fomo.data.local.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class DataRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreferences
) {
    var foodId: Int = 0
    var foodPostMsg: String = ""

    fun getToken(): String {
        return runBlocking { pref.getToken().first() }
    }

    fun setToken(value: String) {
        runBlocking { pref.setToken(value) }
    }

    fun getId(): String {
        return runBlocking { pref.getId().first() }
    }

    fun setId(value: String) {
        runBlocking { pref.setId(value) }
    }

    fun getName(): String {
        return runBlocking { pref.getName().first() }
    }

    fun setName(value: String) {
        runBlocking { pref.setName(value) }
    }

    fun getEmail(): String {
        return runBlocking { pref.getEmail().first() }
    }

    fun setEmail(value: String) {
        runBlocking { pref.setEmail(value) }
    }

    fun getPhoto(): String {
        return runBlocking { pref.getPhoto().first() }
    }

    fun setPhoto(value: String) {
        runBlocking { pref.setPhoto(value) }
    }

    suspend fun getFoods(token: String): List<FoodDataItem?> {
        return try {
            val response = apiService.getFoods("bearer $token")
            response.data!!
        } catch (e: HttpException) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun postFood(
        token: String,
        name: String,
        ingredient: String,
        step: String,
        category: String
    ): FoodPostData? {
        return try {
            val response = apiService.postFood(
                "bearer $token",
                UploadRequest(name, ingredient, step, category)
            )
            foodId = response.data?.id!!
            foodPostMsg = response.message!!
            response.data
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }
    }

    suspend fun patchFood(token: String, file: File): String {
        return try {
            val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("image", file.name, requestFile)
            val response = apiService.patchFood("bearer $token", foodId, body)
            response.message!!
        } catch (e: HttpException) {
            e.printStackTrace()
            ""
        }
    }

    suspend fun deleteFood(token: String, id: Int): String {
        return try {
            val response = apiService.deleteFood("bearer $token", id)
            response.message!!
        } catch (e: HttpException) {
            e.printStackTrace()
            ""
        }
    }

    suspend fun getBookmarks(token: String): List<BookmarkDataItem?> {
        return try {
            val response = apiService.getBookmarks("bearer $token")
            response.data!!
        } catch (e: HttpException) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun postBookmark(token: String, foodId: Int): String {
        return try {
            val response = apiService.postBookmark("bearer $token", BookmarkRequest(foodId))
            response.message!!
        } catch (e: HttpException) {
            e.printStackTrace()
            ""
        }
    }

    suspend fun deleteBookmark(token: String, id: Int): String {
        return try {
            val response = apiService.deleteBookmark("bearer $token", id)
            response.message!!
        } catch (e: HttpException) {
            e.printStackTrace()
            ""
        }
    }

    suspend fun getComments(token: String): List<CommentDataItem?> {
        return try {
            val response = apiService.getComments("bearer $token")
            response.data!!
        } catch (e: HttpException) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun postComment(token: String, foodId: Int, commentField: String): String {
        return try {
            val response =
                apiService.postComment("bearer $token", CommentRequest(foodId, commentField))
            response.message!!
        } catch (e: HttpException) {
            e.printStackTrace()
            ""
        }
    }

    suspend fun getRatings(token: String): List<RatingDataItem?> {
        return try {
            val response = apiService.getRatings("bearer $token")
            response.data!!
        } catch (e: HttpException) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun postRating(token: String, foodId: Int, star: Int): String {
        return try {
            val response =
                apiService.postRating("bearer $token", RatingRequest(foodId, star))
            response.message!!
        } catch (e: HttpException) {
            e.printStackTrace()
            ""
        }
    }

    suspend fun postSearch(token: String, query: String): List<FoodDataItem?> {
        return try {
            val response = apiService.postSearch("bearer $token", SearchRequest(query))
            response.data!!
        } catch (e: HttpException) {
            e.printStackTrace()
            emptyList()
        }
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null

        fun getInstance(apiService: ApiService, pref: UserPreferences): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(apiService, pref).also {
                    instance = it
                }
            }
    }
}