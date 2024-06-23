package com.c241.ps341.fomo.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c241.ps341.fomo.api.response.BookmarkDataItem
import com.c241.ps341.fomo.api.response.CommentDataItem
import com.c241.ps341.fomo.api.response.FoodDataItem
import com.c241.ps341.fomo.api.response.FoodPostData
import com.c241.ps341.fomo.api.response.RatingDataItem
import com.c241.ps341.fomo.data.repository.DataRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
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

    @OptIn(DelicateCoroutinesApi::class)
    fun getFoods(): LiveData<List<FoodDataItem?>> {
        val liveData = MutableLiveData<List<FoodDataItem?>>()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val foods = repository.getFoods(getToken())
                liveData.postValue(foods)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }

    fun postFood(
        name: String,
        ingredient: String,
        step: String,
        category: String
    ): LiveData<FoodPostData?> {
        val liveData = MutableLiveData<FoodPostData?>()

        viewModelScope.launch {
            try {
                val food = repository.postFood(getToken(), name, ingredient, step, category)
                liveData.postValue(food)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }

    fun patchFood(file: File): LiveData<String> {
        val liveData = MutableLiveData<String>()

        viewModelScope.launch {
            try {
                val food = repository.patchFood(getToken(), file)
                liveData.postValue(food)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }

    fun deleteFood(id: Int): LiveData<String> {
        val liveData = MutableLiveData<String>()

        viewModelScope.launch {
            try {
                val food = repository.deleteFood(getToken(), id)
                liveData.postValue(food)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }

    fun getBookmarks(): LiveData<List<BookmarkDataItem?>> {
        val liveData = MutableLiveData<List<BookmarkDataItem?>>()

        viewModelScope.launch {
            try {
                val bookmarks = repository.getBookmarks(getToken())
                liveData.postValue(bookmarks)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }

    fun postBookmark(foodId: Int): LiveData<String> {
        val liveData = MutableLiveData<String>()

        viewModelScope.launch {
            try {
                val bookmark = repository.postBookmark(getToken(), foodId)
                liveData.postValue(bookmark)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }

    fun deleteBookmark(id: Int): LiveData<String> {
        val liveData = MutableLiveData<String>()

        viewModelScope.launch {
            try {
                val bookmark = repository.deleteBookmark(getToken(), id)
                liveData.postValue(bookmark)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }

    fun getComments(): LiveData<List<CommentDataItem?>> {
        val liveData = MutableLiveData<List<CommentDataItem?>>()

        viewModelScope.launch {
            try {
                val comments = repository.getComments(getToken())
                liveData.postValue(comments)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }

    fun postComment(foodId: Int, commentField: String): LiveData<String> {
        val liveData = MutableLiveData<String>()

        viewModelScope.launch {
            try {
                val comment = repository.postComment(getToken(), foodId, commentField)
                liveData.postValue(comment)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }

    fun getRatings(): LiveData<List<RatingDataItem?>> {
        val liveData = MutableLiveData<List<RatingDataItem?>>()

        viewModelScope.launch {
            try {
                val ratings = repository.getRatings(getToken())
                liveData.postValue(ratings)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }

    fun postRating(foodId: Int, star: Int): LiveData<String> {
        val liveData = MutableLiveData<String>()

        viewModelScope.launch {
            try {
                val rating = repository.postRating(getToken(), foodId, star)
                liveData.postValue(rating)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }

    fun postSearch(query: String): LiveData<List<FoodDataItem?>> {
        val liveData = MutableLiveData<List<FoodDataItem?>>()

        viewModelScope.launch {
            try {
                val search = repository.postSearch(getToken(), query)
                liveData.postValue(search)
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        return liveData
    }
}