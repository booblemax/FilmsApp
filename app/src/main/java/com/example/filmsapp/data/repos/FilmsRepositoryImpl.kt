package com.example.filmsapp.data.repos

import com.example.filmsapp.data.remote.FilmsApi
import com.example.filmsapp.data.remote.response.films.BackdropsDto
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.exceptions.RetrofitException
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.ui.base.models.FilmModel
import com.example.filmsapp.util.ConstUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class FilmsRepositoryImpl(
    private val api: FilmsApi
) : FilmsRepository {

    private val cachedFilms = mutableListOf<FilmModel>()

    override suspend fun getLatestFilm(): Resource<FilmModel> =
        withContext(Dispatchers.IO) {
            val response = api.getLatestFilm()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Resource.SUCCESS(body.toModel())
            } else {
                Resource.ERROR<FilmModel>(
                    RetrofitException(response.code(), response.message())
                )
            }
        }

    override suspend fun getPopularFilmsCached(
        page: Int,
        forceUpdate: Boolean
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

    override suspend fun getPopularFilms(
        page: Int
    ): Resource<List<FilmModel>> =
        withContext(Dispatchers.IO) {
            val response = api.getPopularList(page)

            val body = response.body()
            if (response.isSuccessful && body != null) {
                val films = body.results.map { it.toModel() }
                Resource.SUCCESS(films)
            } else {
                Resource.ERROR<List<FilmModel>>(
                    RetrofitException(response.code(), response.message())
                )
            }
        }

    override suspend fun getTopRatedFilmsCached(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> =
        withContext(Dispatchers.IO) {
            if (forceUpdate) {
                cachedFilms.clear()
            }
            if (forceUpdate || cachedFilms.size < page * ConstUtil.PAGE_SIZE) {
                val response = api.getTopRatedList(page)

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

    override suspend fun getTopRatedFilms(
        page: Int
    ): Resource<List<FilmModel>> =
        withContext(Dispatchers.IO) {
            val response = api.getTopRatedList(page)

            val body = response.body()
            if (response.isSuccessful && body != null) {
                val films = body.results.map { it.toModel() }
                Resource.SUCCESS(films)
            } else {
                Resource.ERROR<List<FilmModel>>(
                    RetrofitException(response.code(), response.message())
                )
            }
        }

    override suspend fun getUpcomingFilmsCached(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> =
        withContext(Dispatchers.IO) {
            if (forceUpdate) {
                cachedFilms.clear()
            }
            if (forceUpdate || cachedFilms.size < page * ConstUtil.PAGE_SIZE) {
                val response = api.getUpcomingList(page)

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

    override suspend fun getUpcomingFilms(
        page: Int
    ): Resource<List<FilmModel>> =
        withContext(Dispatchers.IO) {
            val response = api.getUpcomingList(page)

            val body = response.body()
            if (response.isSuccessful && body != null) {
                val films = body.results.map { it.toModel() }
                Resource.SUCCESS(films)
            } else {
                Resource.ERROR<List<FilmModel>>(
                    RetrofitException(response.code(), response.message())
                )
            }
        }

    override suspend fun getFilm(id: String): Resource<FilmModel> =
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
