package com.example.filmsapp.data.repos

import com.example.data.mapper.toModel
import com.example.data.remote.response.films.FilmDto
import com.example.data.remote.response.films.FilmsDto
import com.example.domain.Resource
import com.example.domain.exceptions.RetrofitException
import com.example.domain.models.FilmModel
import com.example.domain.repos.FilmsRepository
import com.example.filmsapp.data.datasource.favorites
import com.example.filmsapp.data.datasource.latest
import com.example.filmsapp.data.datasource.populars
import com.example.filmsapp.data.datasource.toprated
import com.example.filmsapp.data.datasource.upcoming
import kotlinx.coroutines.runBlocking

class FakeFilmsRepository(private val needFailureProvider: () -> Boolean) : FilmsRepository {

    override suspend fun getLatestFilm(): Resource<FilmModel> {
        return getResult(
            { runBlocking { latest } },
            { dto: FilmDto? -> dto?.toModel() }
        )
    }

    override suspend fun getPopularFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> {
        return getResult(
            {
                runBlocking {
                    val range = getIndexRangeForPage(page)
                    FilmsDto(page, populars.subList(range.first, range.last), 100, 100)
                }
            },
            { dto: FilmsDto? -> dto?.results?.map { it.toModel() } }
        )
    }

    override suspend fun getTopRatedFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> {
        return getResult(
            {
                runBlocking {
                    val range = getIndexRangeForPage(page)
                    FilmsDto(page, toprated.subList(range.first, range.last), 100, 100)
                }
            },
            { dto: FilmsDto? -> dto?.results?.map { it.toModel() } }
        )
    }

    override suspend fun getUpcomingFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> {
        return getResult(
            {
                runBlocking {
                    val range = getIndexRangeForPage(page)
                    FilmsDto(page, upcoming.subList(range.first, range.last), 100, 100)
                }
            },
            { dto: FilmsDto? -> dto?.results?.map { it.toModel() } }
        )
    }

    override suspend fun getFilm(id: String, needUpdate: Boolean): Resource<FilmModel> {
        return if (needUpdate) {
            val filmResponse = populars.find { it.id.toString() == id && !needFailureProvider() }
            if (filmResponse != null) {
                val film = filmResponse.toModel()
                Resource.SUCCESS(film)
            } else {
                Resource.ERROR(RetrofitException(404, ""))
            }
        } else {
            val film = populars.find { it.id.toString() == id }
            Resource.SUCCESS(film?.toModel())
        }
    }

    override suspend fun getFavouritesFilms(page: Int): Resource<List<FilmModel>> {
        return Resource.SUCCESS(favorites.map { it.toModel() })
    }

    override suspend fun isFilmStoredInDb(id: String): Boolean {
        return favorites.find { it.id.toString() == id } != null
    }

    override suspend fun saveFilm(film: FilmModel) {
        // filmsDao.insert(film.toDataModel())
    }

    override suspend fun deleteFilm(film: FilmModel) {
        // filmsDao.delete(film.toDataModel())
    }

    override suspend fun searchFilms(query: String, page: Int, needClearCache: Boolean): Resource<List<FilmModel>> {
        return getResult(
            {
                runBlocking {
                    val range = getIndexRangeForPage(page)
                    FilmsDto(page, populars.subList(range.first, range.last), 100, 100)
                }
            },
            { dto: FilmsDto? -> dto?.results?.map { it.toModel() } }
        )
    }

    private fun <R, T> getResult(call: () -> R, mapper: (R?) -> T?): Resource<T> {
        val response = call()
        return if (needFailureProvider()) {
            Resource.ERROR(RetrofitException(404, ""))
        } else Resource.SUCCESS(mapper(response))
    }

    private fun getIndexRangeForPage(page: Int): IntRange =
        if (page == 1) 0..2
        else page..(page + 2)
}
