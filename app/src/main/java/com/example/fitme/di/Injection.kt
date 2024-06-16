package com.example.fitme.di

import android.content.Context
import com.example.fitme.api.ApiConfig
import com.example.fitme.profile.pref.UserPreference
import com.example.fitme.profile.pref.dataStore
import com.example.fitme.FitMeRepository

object Injection {
    fun provideRepository(context: Context): FitMeRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return FitMeRepository.getInstance(pref, apiService)
    }
}