package com.example.fitme.api

import com.example.fitme.api.response.ListHistoryData
import com.example.fitme.api.response.ListHistoryResponse
import com.example.fitme.api.response.LoginResponse
import com.example.fitme.api.response.SignUpResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService{
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("fullname") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("users/{user_id}/predictions")
    suspend fun getListHistory(
        @Path("user_id") userId: String
    ): Response<ResponseBody>
}