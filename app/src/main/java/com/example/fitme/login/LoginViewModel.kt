package com.example.fitme.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.FitMeRepository
import com.example.fitme.profile.model.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: FitMeRepository) : ViewModel() {
    fun login(userID: String) = repository.getUser(userID)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

}