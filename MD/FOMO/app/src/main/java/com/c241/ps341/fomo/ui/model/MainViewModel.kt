package com.c241.ps341.fomo.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.c241.ps341.fomo.api.response.BookmarkDataItem
import com.c241.ps341.fomo.api.response.CommentDataItem
import com.c241.ps341.fomo.api.response.FoodDataItem
import com.c241.ps341.fomo.api.response.FoodPostData
import com.c241.ps341.fomo.api.response.RatingDataItem
import com.c241.ps341.fomo.data.repository.DataRepository
import java.io.File

class MainViewModel(private val repository: DataRepository) : ViewModel() {
    fun foodId(): Int = repository.foodId
    fun foodPostMsg(): String = repository.foodPostMsg

    private fun getToken(): String = repository.getToken()

    fun setToken(value: String) {
        repository.setToken(value)
    }

    fun getId(): String = repository.getId()

    fun setId(value: String) {
        repository.setId(value)
    }

    fun getName(): String = repository.getName()

    fun setName(value: String) {
        repository.setName(value)
    }

    fun getEmail(): String = repository.getEmail()

    fun setEmail(value: String) {
        repository.setEmail(value)
    }

    fun getPhoto(): String = repository.getPhoto()

    fun setPhoto(value: String) {
        repository.setPhoto(value)
    }

    fun getFoods(): LiveData<List<FoodDataItem?>> = repository.getFoods(getToken())
    fun postFood(
        name: String,
        ingredient: String,
        step: String,
        category: String
    ): LiveData<FoodPostData?> = repository.postFood(getToken(), name, ingredient, step, category)

    fun patchFood(file: File): LiveData<String> = repository.patchFood(getToken(), file)
    fun deleteFood(id: Int): LiveData<String> = repository.deleteFood(getToken(), id)
    fun getBookmarks(): LiveData<List<BookmarkDataItem?>> = repository.getBookmarks(getToken())
    fun postBookmark(foodId: Int): LiveData<String> = repository.postBookmark(getToken(), foodId)
    fun deleteBookmark(id: Int): LiveData<String> = repository.deleteBookmark(getToken(), id)
    fun getComments(): LiveData<List<CommentDataItem?>> = repository.getComments(getToken())
    fun postComment(foodId: Int, commentField: String): LiveData<String> = repository.postComment(getToken(), foodId, commentField)
    fun getRatings(): LiveData<List<RatingDataItem?>> = repository.getRatings(getToken())
    fun postRating(foodId: Int, star: Int): LiveData<String> = repository.postRating(getToken(), foodId, star)
    fun postSearch(query: String): LiveData<List<FoodDataItem?>> = repository.postSearch(getToken(), query)
}