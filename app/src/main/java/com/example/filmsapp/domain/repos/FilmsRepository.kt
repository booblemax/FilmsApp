package com.example.filmsapp.domain.repos

import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.models.FilmModel

interface FilmsRepository {

    suspend fun getLatestFilm(): Resource<FilmModel>

    suspend fun getPopularFilmsCached(
        page: Int = 1,
        forceUpdate: Boolean = false
    ): Resource<List<FilmModel>>

    suspend fun getPopularFilms(page: Int = 1): Resource<List<FilmModel>>

    suspend fun getTopRatedFilmsCached(
        page: Int = 1,
        forceUpdate: Boolean = false
    ): Resource<List<FilmModel>>

    suspend fun getTopRatedFilms(page: Int = 1): Resource<List<FilmModel>>

    suspend fun getUpcomingFilmsCached(
        page: Int = 1,
        forceUpdate: Boolean = false
    ): Resource<List<FilmModel>>

    suspend fun getUpcomingFilms(page: Int = 1): Resource<List<FilmModel>>

    suspend fun getFilm(id: String): Resource<FilmModel>
}