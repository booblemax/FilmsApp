package com.example.filmsapp.domain

import com.example.filmsapp.data.remote.FilmsApi
import com.example.filmsapp.data.remote.response.BackdropsDto
import com.example.filmsapp.domain.exceptions.RetrofitException
import com.example.filmsapp.ui.base.models.FilmModel
import com.example.filmsapp.util.ConstUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class FilmsRepository(
    private val api: FilmsApi
) {

    private val cachedFilms = mutableListOf<FilmModel>()

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

    suspend fun getPopularFilms(
        page: Int = 1,
        forceUpdate: Boolean = false
    ): Resource<List<FilmModel>> =
        withContext(Dispatchers.IO) {
            if (forceUpdate) {
                cachedFilms.clear()
            }
            if (forceUpdate || cachedFilms.size < page * ConstUtil.PAGE_SIZE) {
                val response = api.getPopularList(page)

                val body = response.body()
                if (response.isSuccessful && body != null) {
                    val films = body.results.map { it.toModel() }

                    cachedFilms.addAll(films)
                } else {
                    Resource.ERROR<List<FilmModel>>(
                        RetrofitException(response.code(), response.message())
                    )
                }
            }
            Resource.SUCCESS(cachedFilms.toList())
        }

    suspend fun getFilm(id: String): Resource<FilmModel> =
        withContext(Dispatchers.IO) {
            val film = api.getFilm(id)
            var images: BackdropsDto? = null
            supervisorScope {
                launch(coroutineContext) {
                    images = api.getBackdrops(id)
                }
            }
            Resource.SUCCESS(film.toModel(images))
        }
}
