package com.example.filmsapp.ui.base.models

import com.example.filmsapp.data.db.FilmDataModel
import com.example.filmsapp.data.remote.response.films.BackdropsDto
import com.example.filmsapp.data.remote.response.films.Genre

data class FilmModel(
    val id: String = "",
    val title: String = "",
    val poster: String? = "",
    val runtime: Int = 0,
    val releaseDate: String? = "",
    val backdropPath: String? = null,
    val overview: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = -1,
    val genres: List<Genre>? = null,
    val backdrops: BackdropsDto? = null,
    val video: Boolean = false,
    val hasBookmark: Boolean = false
) {

    fun toDataModel() =
        FilmDataModel(
            id,
            title,
            poster,
            runtime,
            releaseDate,
            backdropPath,
            overview,
            voteAverage,
            voteCount,
            genres,
            backdrops,
            video
        )
}
