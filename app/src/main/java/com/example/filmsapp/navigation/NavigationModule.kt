package com.example.filmsapp.navigation

import com.github.terrakok.cicerone.Cicerone
import org.koin.dsl.module

val navigationModule = module {

    val cicerone = Cicerone.create()

    factory { cicerone.router }

    single { cicerone.getNavigatorHolder() }
}