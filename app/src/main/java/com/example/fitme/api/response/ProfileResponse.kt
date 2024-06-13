package com.example.fitme.api.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: ProfileData,
)

data class ProfileData(

    @field:SerializedName("user_id")
    val userID: String,

    @field:SerializedName("full_name")
    val fullName: String,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("email")
    val email: String,
)