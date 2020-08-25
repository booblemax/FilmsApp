package com.example.data.remote.response.films

import com.google.gson.annotations.SerializedName

data class FilmDto(
    @SerializedName("adult")
    val adult: Boolean = false,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: Any? = null,
    @SerializedName("budget")
    val budget: Int = 0,
    @SerializedName("genres")
    val genres: List<GenreDto> = listOf(),
    @SerializedName("homepage")
    val homepage: String = "",
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("imdb_id")
    val imdbId: String = "",
    @SerializedName("original_language")
    val originalLanguage: String = "",
    @SerializedName("original_title")
    val originalTitle: String = "",
    @SerializedName("overview")
    val overview: String = "",
    @SerializedName("popularity")
    val popularity: Double = -1.0,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("production_companies")
    val productionCompanies: List<Any> = listOf(),
    @SerializedName("production_countries")
    val productionCountries: List<Any> = listOf(),
    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("revenue")
    val revenue: Int = -1,
    @SerializedName("runtime")
    val runtime: Int = -1,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<Any> = listOf(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("tagline")
    val tagline: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("video")
    val video: Boolean = false,
    @SerializedName("vote_average")
    val voteAverage: Double = -1.0,
    @SerializedName("vote_count")
    val voteCount: Int = 0
)
