package com.example.fitme.api.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: PredictData
)

data class PredictData(
    @SerializedName("face_shape")
    val faceShape: String,

    @SerializedName("seasonal_type")
    val seasonalType: String,

    @SerializedName("face_shape_confidence_score")
    val faceShapeConfidenceScore: Double,

    @SerializedName("seasonal_type_confidence_score")
    val seasonalTypeConfidenceScore: Double,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("response_images")
    val responseImages: List<String>,

    @SerializedName("image_url")
    val imageUrl: String
)
