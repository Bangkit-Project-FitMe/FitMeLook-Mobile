package com.example.fitme.di

import android.content.Context
import com.example.fitme.api.ApiConfig
import com.example.fitme.profile.pref.UserPreference
import com.example.fitme.profile.pref.dataStore
import com.example.fitme.repository.FitMeRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): FitMeRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking {
            pref.getSession().first()
        }
        val apiService = ApiConfig.getApiService(user.token)
        return FitMeRepository.getInstance(pref, apiService)
    }
}