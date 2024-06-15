package com.example.fitme.api.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse (

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,
)