package com.example.data.di

import com.example.data.Urls
import com.example.data.interceptor.AuthHeaderInterceptor
import com.example.data.remote.FilmsApi
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeRequestInitializer
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

val netModule = module {

    fun configRetrofit(client: OkHttpClient, gson: Gson, baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    fun <T> createService(retrofit: Retrofit, clazz: Class<T>) = retrofit.create(clazz)

    fun configClient(
        httpInterceptor: HttpLoggingInterceptor,
        authHeaderInterceptor: AuthHeaderInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpInterceptor)
            .addInterceptor(authHeaderInterceptor)
            .build()

    fun configYoutubeClient(credential: GoogleAccountCredential): YouTube {
        val transport = AndroidHttp.newCompatibleTransport()
        val factory = JacksonFactory.getDefaultInstance()
        return YouTube.Builder(transport, factory, credential)
            .setApplicationName("FilmsApp")
            .setYouTubeRequestInitializer(YouTubeRequestInitializer(BuildConfig.GOOGLE_API_KEY))
            .build()
    }

    factory { Gson() }

    single { configClient(get(), get()) }

    factory {
        HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Timber.tag("OkHttp").d(message)
                }
            }
        ).setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    factory { AuthHeaderInterceptor() }

    single { configRetrofit(get<OkHttpClient>(), get<Gson>(), Urls.BASE_URL) }

    factory { createService(get(), FilmsApi::class.java) }

}