package com.example.filmsapp.data.di

import androidx.room.Room
import com.example.filmsapp.data.db.FilmsDb
import org.koin.dsl.module

val dbModule = module {

    single { Room.databaseBuilder(get(), FilmsDb::class.java, FilmsDb.DATABASE_NAME).build() }

    factory { get<FilmsDb>().filmsDao() }
}
