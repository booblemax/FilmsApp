package com.example.filmsapp.data.repos

import com.example.filmsapp.data.remote.FilmsApi
import com.example.filmsapp.data.remote.response.films.FilmDto
import com.example.filmsapp.data.remote.response.films.FilmsDto
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.exceptions.RetrofitException
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class FakeFilmsRepository(
    private val filmsApi: FilmsApi
) : FilmsRepository {

    override suspend fun getLatestFilm(): Resource<FilmModel> {
        return getResult(
            { runBlocking { filmsApi.getLatestFilm() } },
            { dto: FilmDto? -> dto?.toModel() }
        )
    }

    override suspend fun getPopularFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> {
        return getResult(
            { runBlocking { filmsApi.getPopularList(page) } },
            { dto: FilmsDto? -> dto?.results?.map { it.toModel() } }
        )
    }

    override suspend fun getTopRatedFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> {
        return getResult(
            { runBlocking { filmsApi.getTopRatedList(page) } },
            { dto: FilmsDto? -> dto?.results?.map { it.toModel() } }
        )
    }

    override suspend fun getUpcomingFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> {
        return getResult(
            { runBlocking { filmsApi.getUpcomingList(page) } },
            { dto: FilmsDto? -> dto?.results?.map { it.toModel() } }
        )
    }

    override suspend fun getFilm(id: String): Resource<FilmModel> {
        return getResult(
            { runBlocking { filmsApi.getFilm(id) } },
            { dto: FilmDto? -> dto?.toModel() }
        )
    }

    private fun <R, T> getResult(call: () -> Response<R>, mapper: (R?) -> T?): Resource<T> {
        val response = call()
        return if (response.isSuccessful) {
            Resource.SUCCESS(mapper(response.body()))
        } else Resource.ERROR(RetrofitException(response.code(), response.message()))
    }
 }