package com.example.filmsapp.data.remote.response.youtube

import com.google.gson.annotations.SerializedName

data class Id(
    @SerializedName("channelId")
    val channelId: String,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("playlistId")
    val playlistId: String,
    @SerializedName("videoId")
    val videoId: String
)
