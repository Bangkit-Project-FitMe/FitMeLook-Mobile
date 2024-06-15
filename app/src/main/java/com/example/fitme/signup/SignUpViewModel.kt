package com.example.fitme.signup

import androidx.lifecycle.ViewModel
import com.example.fitme.FitMeRepository

class SignUpViewModel(private val repository: FitMeRepository): ViewModel() {

    fun signUp(userID: String, email: String, fullName: String) =
        repository.signUp(userID, email, fullName)
}