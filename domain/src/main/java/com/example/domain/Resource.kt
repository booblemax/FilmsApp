package com.example.domain

sealed class Resource<T> {
    data class SUCCESS<T>(val data: T? = null) : Resource<T>()
    class ERROR<T>(val error: Exception? = null) : Resource<T>()
    class LOADING<T> : Resource<T>()
}
