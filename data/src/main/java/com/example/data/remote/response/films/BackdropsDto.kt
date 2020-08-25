package com.example.data.remote.response.films

import com.google.gson.annotations.SerializedName

data class BackdropsDto(
    @SerializedName("backdrops")
    val backdrops: List<BackdropDto>,
    @SerializedName("id")
    val id: Int
)
