package com.example.filmsapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.filmsapp.data.remote.response.films.BackdropsDto
import com.example.filmsapp.data.remote.response.films.Genre

@Entity(tableName = "films")
data class FilmDataModel(
    @PrimaryKey val id: String = "",
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
    val video: Boolean = false
)
