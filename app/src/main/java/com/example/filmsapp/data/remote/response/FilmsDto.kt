package com.example.filmsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class FilmsDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<FilmDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
