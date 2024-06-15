package com.example.fitme.prediction.model

import android.os.Parcel
import android.os.Parcelable

data class PredictionModel (
    val faceShape: String,
    val seasonalType: String,
    val faceShapeConfidenceScore: Double,
    val seasonalTypeConfidenceScore: Double,
    val createdAt: String,
    val responseImages: List<String>,
    val imageUrl: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(faceShape)
        parcel.writeString(seasonalType)
        parcel.writeDouble(faceShapeConfidenceScore)
        parcel.writeDouble(seasonalTypeConfidenceScore)
        parcel.writeString(createdAt)
        parcel.writeStringList(responseImages)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PredictionModel> {
        override fun createFromParcel(parcel: Parcel): PredictionModel {
            return PredictionModel(parcel)
        }

        override fun newArray(size: Int): Array<PredictionModel?> {
            return arrayOfNulls(size)
        }
    }
}
