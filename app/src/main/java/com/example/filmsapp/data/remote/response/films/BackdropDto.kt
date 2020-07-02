package com.example.filmsapp.data.remote.response.films

import com.google.gson.annotations.SerializedName

data class BackdropDto(
    @SerializedName("aspect_ratio")
    val aspectRatio: Double,
    @SerializedName("file_path")
    val filePath: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("iso_639_1")
    val iso6391: Any?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Double,
    @SerializedName("width")
    val width: Int
)
