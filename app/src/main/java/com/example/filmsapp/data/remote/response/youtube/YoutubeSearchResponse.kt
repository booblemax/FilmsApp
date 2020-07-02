package com.example.filmsapp.data.remote.response.youtube


import com.google.gson.annotations.SerializedName

data class YoutubeSearchResponse(
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("kind")
    val kind: String
)