package com.example.fitme.profile.model

data class UserModel (
    val fullname: String,
    val email: String,
    val password: String,
    val token: String,
    val isLogin: Boolean = false
)