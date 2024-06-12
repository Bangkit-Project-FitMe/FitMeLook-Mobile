package com.example.fitme.api

import com.example.fitme.api.response.ProfileResponse
import com.example.fitme.api.response.SignUpResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService{
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("userID") userID: String,
        @Field("email") email: String,
        @Field("full_name") fullName: String,
    ): SignUpResponse

    @GET("users/{userID}")
    suspend fun getUser(
        @Path("userID") userID: String
    ): ProfileResponse
}