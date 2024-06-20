package com.c241.ps341.fomo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    var photoUrl: String = ""

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

    fun getFoods(token: String): LiveData<List<FoodDataItem?>> {
        val value = MutableLiveData<List<FoodDataItem?>>()

        runBlocking {
            try {
                val response = apiService.getFoods("bearer $token")
                value.value = response.data!!
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
    }

    fun postFood(
        token: String,
        name: String,
        ingredient: String,
        step: String,
        category: String
    ): LiveData<FoodPostData?> {
        val value = MutableLiveData<FoodPostData?>()

        runBlocking {
            try {
                val response = apiService.postFood("bearer $token", UploadRequest(name, ingredient, step, category))
                foodId = response.data?.id!!
                foodPostMsg = response.message!!
                value.value = response.data
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
    }

    fun patchFood(token: String, file: File): LiveData<String> {
        val value = MutableLiveData<String>()

        runBlocking {
            try {
                val requestFile: RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body: MultipartBody.Part =
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                val response = apiService.patchFood("bearer $token", foodId, body)
                photoUrl = response.data?.image!!
                value.value = response.message!!
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
    }

    fun deleteFood(token: String, id: Int): LiveData<String> {
        val value = MutableLiveData<String>()

        runBlocking {
            try {
                val response = apiService.deleteFood("bearer $token", id)
                value.value = response.message!!
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
    }

    fun getBookmarks(token: String): LiveData<List<BookmarkDataItem?>> {
        val value = MutableLiveData<List<BookmarkDataItem?>>()

        runBlocking {
            try {
                val response = apiService.getBookmarks("bearer $token")
                value.value = response.data!!
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
    }

    fun postBookmark(token: String, foodId: Int): LiveData<String> {
        val value = MutableLiveData<String>()

        runBlocking {
            try {
                val response = apiService.postBookmark("bearer $token", BookmarkRequest(foodId))
                value.value = response.message!!
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
    }

    fun deleteBookmark(token: String, id: Int): LiveData<String> {
        val value = MutableLiveData<String>()

        runBlocking {
            try {
                val response = apiService.deleteBookmark("bearer $token", id)
                value.value = response.message!!
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
    }

    fun getComments(token: String): LiveData<List<CommentDataItem?>> {
        val value = MutableLiveData<List<CommentDataItem?>>()

        runBlocking {
            try {
                val response = apiService.getComments("bearer $token")
                value.value = response.data!!
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
    }

    fun postComment(token: String, foodId: Int, commentField: String): LiveData<String> {
        val value = MutableLiveData<String>()

        runBlocking {
            try {
                val response = apiService.postComment("bearer $token", CommentRequest(foodId, commentField))
                value.value = response.message!!
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
    }

    fun getRatings(token: String): LiveData<List<RatingDataItem?>> {
        val value = MutableLiveData<List<RatingDataItem?>>()

        runBlocking {
            try {
                val response = apiService.getRatings("bearer $token")
                value.value = response.data!!
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
    }

    fun postRating(token: String, foodId: Int, star: Int): LiveData<String> {
        val value = MutableLiveData<String>()

        runBlocking {
            try {
                val response = apiService.postRating("bearer $token", RatingRequest(foodId, star))
                value.value = response.message!!
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
    }

    fun postSearch(token: String, query: String): LiveData<List<FoodDataItem?>> {
        val value = MutableLiveData<List<FoodDataItem?>>()

        runBlocking {
            try {
                val response = apiService.postSearch("bearer $token", SearchRequest(query))
                value.value = response.data!!
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return value
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