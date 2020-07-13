package com.example.filmsapp.data.remote.response.youtube

import com.google.gson.annotations.SerializedName

data class Thumbnail(
    @SerializedName("height")
    val height: Long?,
    @SerializedName("width")
    val width: Long?,
    @SerializedName("url")
    val url: String?
)
