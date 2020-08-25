package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.domain.models.BackdropsModel
import com.example.domain.models.GenreModel

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
    val genres: List<GenreModel>? = null,
    val backdrops: BackdropsModel? = null,
    val video: Boolean = false
)
