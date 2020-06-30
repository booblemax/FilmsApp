package com.example.filmsapp.ui.base.common.networkinfo

import androidx.lifecycle.LiveData

object NetworkLiveState: LiveData<Event>() {

    internal fun notify(event: Event) {
        postValue(event)
    }
}