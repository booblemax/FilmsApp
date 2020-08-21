package com.example.filmsapp.data.repos

import com.example.data.db.FilmsDao
import com.example.domain.Resource
import com.example.domain.exceptions.RetrofitException
import com.example.domain.models.FilmModel
import com.example.domain.repos.FilmsRepository
import com.example.filmsapp.data.remote.FilmsApi
import com.example.filmsapp.data.remote.response.films.FilmDto
import com.example.filmsapp.data.remote.response.films.FilmsDto
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class FakeFilmsRepository(
    private val filmsDao: FilmsDao,
    private val filmsApi: FilmsApi
) : FilmsRepository {

    override suspend fun getLatestFilm(): Resource<FilmModel> {
        return getResult(
            { runBlocking { filmsApi.getLatestFilm() } },
            { dto: FilmDto? -> dto.toModel() }
        )
    }

    override suspend fun getPopularFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> {
        return getResult(
            { runBlocking { filmsApi.getPopularList(page) } },
            { dto: FilmsDto? -> dto.results.map { it.toModel() } }
        )
    }

    override suspend fun getTopRatedFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> {
        return getResult(
            { runBlocking { filmsApi.getTopRatedList(page) } },
            { dto: FilmsDto? -> dto.results.map { it.toModel() } }
        )
    }

    override suspend fun getUpcomingFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> {
        return getResult(
            { runBlocking { filmsApi.getUpcomingList(page) } },
            { dto: FilmsDto? -> dto.results.map { it.toModel() } }
        )
    }

    override suspend fun getFilm(id: String, needUpdate: Boolean): Resource<FilmModel> {
        return if (needUpdate) {
            val filmResponse = filmsApi.getFilm(id)
            if (filmResponse.isSuccessful && filmResponse.body() != null) {
                val film = filmResponse.body().toModel()
                filmsDao.update(film.toDataModel()!!)
                Resource.SUCCESS(film)
            } else {
                Resource.ERROR(RetrofitException(filmResponse.code(), filmResponse.message()))
            }
        } else {
            val film = filmsDao.getFilm(id)
            Resource.SUCCESS(film?.toModel())
        }
    }

    override suspend fun getFavouritesFilms(page: Int): Resource<List<FilmModel>> {
        return Resource.SUCCESS(filmsDao.getFilms().map { it.toModel() })
    }

    override suspend fun isFilmStoredInDb(id: String): Boolean {
        return filmsDao.contains(id) > 0
    }

    override suspend fun saveFilm(film: FilmModel) {
        filmsDao.insert(film.toDataModel())
    }

    override suspend fun deleteFilm(film: FilmModel) {
        filmsDao.delete(film.toDataModel())
    }

    private fun <R, T> getResult(call: () -> Response<R>, mapper: (R?) -> T?): Resource<T> {
        val response = call()
        return if (response.isSuccessful) {
            Resource.SUCCESS(mapper(response.body()))
        } else Resource.ERROR(RetrofitException(response.code(), response.message()))
    }
}
