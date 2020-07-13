package com.example.filmsapp.data.remote.response.youtube

import com.google.gson.annotations.SerializedName

data class Thumbnails(
    @SerializedName("default")
    val default: Thumbnail,
    @SerializedName("high")
    val high: Thumbnail,
    @SerializedName("maxres")
    val maxres: Thumbnail,
    @SerializedName("medium")
    val medium: Thumbnail,
    @SerializedName("standard")
    val standard: Thumbnail
)
