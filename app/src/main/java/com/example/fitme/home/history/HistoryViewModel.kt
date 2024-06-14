package com.example.fitme.home.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitme.api.ApiService
import com.example.fitme.api.response.ListHistoryData
import com.example.fitme.api.response.ListHistoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class HistoryViewModel : ViewModel() {
    private val _historyData = MutableLiveData<List<ListHistoryData>>()
    val historyData: LiveData<List<ListHistoryData>> get() = _historyData

    fun fetchHistoryData(apiService: ApiService, userId: String) {
        viewModelScope.launch {
            try {
                val response: Response<ResponseBody> = apiService.getListHistory(userId)
                val rawJson = response.body()?.string()
                Log.d("HistoryViewModel", "Raw JSON Response: $rawJson")

                if (rawJson != null) {
                    val jsonObject = JSONObject(rawJson)
                    val status = jsonObject.getString("status")

                    if (status == "success") {
                        val listHistoryResponse = Gson().fromJson(rawJson, ListHistoryResponse::class.java)
                        _historyData.postValue(listHistoryResponse.data)
                    } else {
                        val message = jsonObject.getString("message")
                        Log.e("HistoryViewModel", "Error message from API: $message")
                        _historyData.postValue(emptyList())
                    }
                }
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Error fetching data", e)
            }
        }
    }
}

