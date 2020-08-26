package com.example.domain.repos

import com.example.domain.Resource
import com.example.domain.models.FilmModel

interface FilmsRepository {

    suspend fun getLatestFilm(): Resource<FilmModel>

    suspend fun getPopularFilms(
        page: Int = 1,
        forceUpdate: Boolean = false
    ): Resource<List<FilmModel>>

    suspend fun getTopRatedFilms(
        page: Int = 1,
        forceUpdate: Boolean = false
    ): Resource<List<FilmModel>>

    suspend fun getUpcomingFilms(
        page: Int = 1,
        forceUpdate: Boolean = false
    ): Resource<List<FilmModel>>

    suspend fun getFavouritesFilms(
        page: Int = 1
    ): Resource<List<FilmModel>>

    suspend fun isFilmStoredInDb(id: String): Boolean

    suspend fun getFilm(id: String, needUpdate: Boolean): Resource<FilmModel>

    suspend fun saveFilm(film: FilmModel)

    suspend fun deleteFilm(film: FilmModel)

    suspend fun searchFilms(query: String, page: Int, needClearCache: Boolean): Resource<List<FilmModel>>
}
