package com.example.filmsapp.domain

import com.example.filmsapp.data.remote.FilmsApi
import com.example.filmsapp.domain.exceptions.RetrofitException
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class FilmsRepository(
    private val api: FilmsApi
) {

    suspend fun getLatestFilm(): Resource<FilmModel> =
        withContext(Dispatchers.IO) {
            val response = api.getLatestFilm()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Resource.SUCCESS(body.toModel())
            } else {
                Resource.ERROR<FilmModel>(RetrofitException(response.code(), response.message()))
            }
        }

    suspend fun getPopularFilm(page: Int = 1): Resource<List<FilmModel>> =
        withContext(Dispatchers.IO) {
            val response = api.getPopularList(page)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Resource.SUCCESS(body.results.map { it.toModel() })
            } else {
                Resource.ERROR<List<FilmModel>>(RetrofitException(response.code(), response.message()))
            }
        }

    suspend fun getFilm(id: String): Resource<FilmModel> =
        withContext(Dispatchers.IO) {
            val filmDeferred = async { api.getFilm(id) }
            val imagesDeferred = async { api.getBackdrops(id) }
            val film = filmDeferred.await()
            val images = imagesDeferred.await()
            val filmModel = film.toModel(images)
            Resource.SUCCESS(filmModel)
        }
}