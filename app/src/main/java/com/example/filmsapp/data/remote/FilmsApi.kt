package com.example.filmsapp.data.remote

import com.example.filmsapp.data.remote.response.films.BackdropsDto
import com.example.filmsapp.data.remote.response.films.FilmDto
import com.example.filmsapp.data.remote.response.films.FilmsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmsApi {

    @GET("movie/latest")
    suspend fun getLatestFilm(): Response<FilmDto>

    @GET("movie/popular")
    suspend fun getPopularList(
        @Query("page")
        page: Int
    ): Response<FilmsDto>

    @GET("movie/top_rated")
    suspend fun getTopRatedList(
        @Query("page")
        page: Int
    ): Response<FilmsDto>

    @GET("movie/upcoming")
    suspend fun getUpcomingList(
        @Query("page")
        page: Int
    ): Response<FilmsDto>

    @GET("movie/{id}")
    suspend fun getFilm(
        @Path("id")
        id: String
    ): Response<FilmDto>

    @GET("movie/{id}/images")
    suspend fun getBackdrops(
        @Path("id")
        id: String
    ): BackdropsDto

    @GET("search/movie")
    suspend fun searchFilms(@Query("query") query: String, @Query("page") page: Int): Response<FilmsDto>
}
