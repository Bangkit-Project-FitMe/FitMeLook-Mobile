package com.example.fitme.repository

import com.example.fitme.profile.pref.UserPreference
import com.example.fitme.api.ApiService
import com.example.fitme.profile.model.UserModel
import kotlinx.coroutines.flow.Flow

class FitMeRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
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