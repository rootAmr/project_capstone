package com.c241.ps341.fomo.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val token = stringPreferencesKey("user_token")
    private val id = stringPreferencesKey("user_id")
    private val name = stringPreferencesKey("user_name")
    private val email = stringPreferencesKey("user_email")
    private val photo = stringPreferencesKey("user_photo")

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[token] ?: ""
        }
    }

    suspend fun setToken(value: String) {
        dataStore.edit { preferences ->
            preferences[token] = value
        }
    }

    fun getId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[id] ?: ""
        }
    }

    suspend fun setId(value: String) {
        dataStore.edit { preferences ->
            preferences[id] = value
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[name] ?: ""
        }
    }

    suspend fun setName(value: String) {
        dataStore.edit { preferences ->
            preferences[name] = value
        }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[email] ?: ""
        }
    }

    suspend fun setEmail(value: String) {
        dataStore.edit { preferences ->
            preferences[email] = value
        }
    }

    fun getPhoto(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[photo] ?: ""
        }
    }

    suspend fun setPhoto(value: String) {
        dataStore.edit { preferences ->
            preferences[photo] = value
        }
    }

    companion object {
        @Volatile
        private var instance: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences {
            return instance ?: synchronized(this) {
                val instance = UserPreferences(context.dataStore)
                this.instance = instance
                instance
            }
        }
    }
}