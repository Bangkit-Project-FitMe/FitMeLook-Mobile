package com.example.fitme.prediction

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.fitme.FitMeRepository
import com.example.fitme.profile.model.UserModel
import java.io.File

class PredictionViewModel(private val repository: FitMeRepository): ViewModel() {
    fun predict(userID: String, file: File) = repository.predict(userID, file)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}