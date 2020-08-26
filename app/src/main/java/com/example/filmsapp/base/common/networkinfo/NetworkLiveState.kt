package com.example.filmsapp.base.common.networkinfo

import androidx.lifecycle.LiveData

object NetworkLiveState : LiveData<Event>() {

    internal fun notify(event: Event) {
        postValue(event)
    }
}
