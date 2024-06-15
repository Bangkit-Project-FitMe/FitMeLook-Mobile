package com.example.fitme.api.response

import com.google.gson.annotations.SerializedName

data class DetailHistoryResponse(
    @field:SerializedName("data")
    val data: DetailHistoryData,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class DetailHistoryData(
    @field:SerializedName("created_at")
    val created_at: String,

    @field:SerializedName("face_shape")
    val face_shape: String,

    @field:SerializedName("face_shape_confidence_score")
    val face_shape_confidence_score: Double,

    @field:SerializedName("image_url")
    val image_url: String,

    @field:SerializedName("prediction_id")
    val prediction_id: String,

    @field:SerializedName("response_images")
    val response_images: List<String>,

    @field:SerializedName("seasonal_type")
    val seasonal_type: String,

    @field:SerializedName("seasonal_type_confidence_score")
    val seasonal_type_confidence_score: Double
)