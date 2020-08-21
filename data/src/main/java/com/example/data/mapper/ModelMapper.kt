package com.example.data.mapper

import com.example.data.db.FilmDataModel
import com.example.data.remote.response.films.BackdropDto
import com.example.data.remote.response.films.BackdropsDto
import com.example.data.remote.response.films.FilmDto
import com.example.data.remote.response.films.GenreDto
import com.example.domain.models.BackdropModel
import com.example.domain.models.BackdropsModel
import com.example.domain.models.FilmModel
import com.example.domain.models.GenreModel

fun FilmDto.toModel(backdrops: BackdropsDto? = null) =
    FilmModel(
        id.toString(),
        title,
        posterPath,
        runtime,
        releaseDate,
        backdropPath,
        overview,
        voteAverage,
        voteCount,
        genres.map { it.toModel() },
        backdrops?.toModel(),
        video
    )

fun FilmDataModel.toModel(): FilmModel {
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

fun FilmModel.toDataModel() =
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

fun GenreDto.toModel() =
    GenreModel(id, name)

fun BackdropDto.toModel() =
    BackdropModel(aspectRatio, filePath, height, width)

fun BackdropsDto.toModel() =
    BackdropsModel(id, backdrops.map { it.toModel() })
