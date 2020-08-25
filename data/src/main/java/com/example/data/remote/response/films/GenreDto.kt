package com.example.data.remote.response.films

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenreDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
) : Parcelable
