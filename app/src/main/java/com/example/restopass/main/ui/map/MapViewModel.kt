package com.example.restopass.main.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is map Fragment"
    }
    val text: MutableLiveData<String> = _text

}

data class Filters(var tags: List<String>, var plan: String, var search: String)