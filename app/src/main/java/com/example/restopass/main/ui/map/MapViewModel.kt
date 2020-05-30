package com.example.restopass.main.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    var filters: Filters? = null
    var selectedFilters: SelectedFilters = SelectedFilters()
}

data class SelectedFilters(var tags: MutableList<String> = mutableListOf(), var plan: String? = null, var search: String? = null)
data class Filters(var tags: List<String>, var plans: List<String>)