package com.example.filmsapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.filmsapp.data.remote.response.films.BackdropsDto
import com.example.filmsapp.data.remote.response.films.Genre
import com.example.filmsapp.ui.base.models.FilmModel

@Entity(tableName = "films")
@TypeConverters(FilmsTypeConverter::class)
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
) {

    fun toModel(): FilmModel {
        return FilmModel(
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
}
