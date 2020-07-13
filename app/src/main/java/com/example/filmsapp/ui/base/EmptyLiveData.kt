package com.example.filmsapp.ui.base

import androidx.lifecycle.MutableLiveData

class EmptyLiveData : MutableLiveData<Any>() {

    fun post() {
        postValue(true)
    }
}
