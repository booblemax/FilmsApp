package com.example.data.db

import androidx.room.TypeConverter
import com.example.domain.models.BackdropsModel
import com.example.domain.models.GenreModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class FilmsTypeConverter {

    @TypeConverter
    fun fromGenres(genres: List<GenreModel>): String = Gson().toJson(genres)

    @TypeConverter
    fun toGenres(genres: String): List<GenreModel> {
        val listType: Type = object : TypeToken<List<GenreModel>>() {}.type
        return Gson().fromJson(genres, listType)
    }

    @TypeConverter
    fun fromBackdrops(backdropsModel: BackdropsModel): String = Gson().toJson(backdropsModel)

    @TypeConverter
    fun toBackdrops(backdrops: String): BackdropsModel =
        Gson().fromJson(backdrops, BackdropsModel::class.java)
}
