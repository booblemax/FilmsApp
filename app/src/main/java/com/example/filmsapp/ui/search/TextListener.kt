package com.example.filmsapp.ui.search

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
class TextListener : SearchView.OnQueryTextListener {

    private val _channel = MutableStateFlow("")
    val channel: StateFlow<String> get() = _channel

    override fun onQueryTextChange(newText: String?): Boolean = postQuery(newText)

    override fun onQueryTextSubmit(query: String?): Boolean = postQuery(query)

    private fun postQuery(newText: String?) =
        newText?.let {
            if (_channel.value != it) {
                _channel.value = it
                true
            } else {
                false
            }
        } ?: false
}
