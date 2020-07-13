package com.example.filmsapp.data.remote.response.youtube

import com.google.gson.annotations.SerializedName

data class Key(
    @SerializedName("height")
    val height: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: String
)
