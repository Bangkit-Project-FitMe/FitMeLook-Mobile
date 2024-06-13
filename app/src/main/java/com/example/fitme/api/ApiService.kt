package com.example.fitme.api

import com.example.fitme.api.response.PredictionResponse
import com.example.fitme.api.response.ProfileResponse
import com.example.fitme.api.response.SignUpResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Part
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

    @Multipart
    @POST("users/{userID}/predict")
    suspend fun predict(
        @Path("userID") userID: String,
        @Part image: MultipartBody.Part
    ): PredictionResponse
}