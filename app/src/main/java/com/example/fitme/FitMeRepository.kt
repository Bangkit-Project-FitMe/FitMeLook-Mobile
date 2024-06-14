package com.example.fitme

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.fitme.profile.pref.UserPreference
import com.example.fitme.api.ApiService
import com.example.fitme.api.ResultState
import com.example.fitme.api.response.ErrorResponse
import com.example.fitme.api.response.PredictionResponse
import com.example.fitme.api.response.ProfileResponse
import com.example.fitme.api.response.SignUpResponse
import com.example.fitme.profile.model.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class FitMeRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun signUp(userID: String, email: String, fullName: String): LiveData<ResultState<SignUpResponse>> = liveData{
        emit(ResultState.Loading)
        try {
            val response = apiService.register(userID, email, fullName)
            emit(ResultState.Success(response))
        }catch (e: HttpException){
            val body = Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)
            emit(ResultState.Error(body.message))
        }
    }

    fun getUser(userID: String): LiveData<ResultState<ProfileResponse>> = liveData{
        emit(ResultState.Loading)
        try {
            val response = apiService.getUser(userID)
            emit(ResultState.Success(response))
        }catch (e: HttpException){
            val body = Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)
            emit(ResultState.Error(body.message))
        }
    }

    fun predict(userID: String, imageFile: File): LiveData<ResultState<PredictionResponse>> = liveData{
        emit(ResultState.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )
        try {
            val response = apiService.predict(userID, multipartBody)
            emit(ResultState.Success(response))
        }catch (e: HttpException){
            val body = Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)
            emit(ResultState.Error(body.message))
        }
    }

    companion object {
        @Volatile
        private var instance: FitMeRepository? = null
        fun clearInstance(){
            instance = null
        }
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): FitMeRepository =
            instance ?: synchronized(this) {
                instance ?: FitMeRepository(userPreference, apiService)
            }.also { instance = it }
    }
}