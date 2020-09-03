package com.example.filmsapp.base.mvi

open class BaseIntent : Intention {
    class Empty : BaseIntent()
    class Error(val error: Throwable) : BaseIntent()
}
