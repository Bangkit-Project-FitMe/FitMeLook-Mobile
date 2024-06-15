package com.example.fitme.profile.model

data class UserModel (
    val userID: String,
    val email: String,
    val fullName: String,
    val isLogin: Boolean = false
)