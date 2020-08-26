package com.example.data.di

import androidx.room.Room
import com.example.data.db.FilmsDb
import org.koin.dsl.module

val dbModule = module {

    single { Room.databaseBuilder(get(), FilmsDb::class.java, FilmsDb.DATABASE_NAME).build() }

    factory { get<FilmsDb>().filmsDao() }
}
