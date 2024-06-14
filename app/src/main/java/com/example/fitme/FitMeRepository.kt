package com.example.fitme

import android.util.Log
import com.example.fitme.profile.pref.UserPreference
import com.example.fitme.api.ApiService
import com.example.fitme.profile.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FitMeRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession().map { user ->
            Log.d("FitMeRepository", "Retrieved token in repository: ${user.token}")
            user
        }
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: FitMeRepository? = null
        fun clearInstance(){
            instance = null
        }
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): FitMeRepository =
            instance ?: synchronized(this) {
                instance ?: FitMeRepository(userPreference, apiService)
            }.also { instance = it }
    }
}