package com.example.filmsapp.data.db

import androidx.room.TypeConverter
import com.example.filmsapp.data.remote.response.films.BackdropsDto
import com.example.filmsapp.data.remote.response.films.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class FilmsTypeConverter {

    @TypeConverter
    fun fromGenres(genres: List<Genre>): String = Gson().toJson(genres)

    @TypeConverter
    fun toGenres(genres: String): List<Genre> {
        val listType: Type = object : TypeToken<List<Genre>>() {}.type
        return Gson().fromJson<List<Genre>>(genres, listType)
    }

    @TypeConverter
    fun fromBackdrops(backdropsDto: BackdropsDto): String = Gson().toJson(backdropsDto)

    @TypeConverter
    fun toBackdrops(backdrops: String): BackdropsDto =
        Gson().fromJson(backdrops, BackdropsDto::class.java)
}
