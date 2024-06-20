package com.c241.ps341.fomo.data

import android.content.Context
import android.util.Log
import com.c241.ps341.fomo.api.ApiConfig
import com.c241.ps341.fomo.data.local.UserPreferences
import com.c241.ps341.fomo.data.repository.DataRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun getRepository(context: Context): DataRepository {
        val pref = UserPreferences.getInstance(context)
        val apiService = ApiConfig.getApiService()
        return DataRepository.getInstance(apiService, pref)
    }
}