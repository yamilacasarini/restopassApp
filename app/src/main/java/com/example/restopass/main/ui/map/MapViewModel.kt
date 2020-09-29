package com.example.restopass.main.ui.map

import androidx.lifecycle.ViewModel
import com.example.restopass.domain.Membership
import com.example.restopass.service.RestaurantService

class MapViewModel : ViewModel() {
    lateinit var filters: Filters
    var selectedFilters: SelectedFilters = SelectedFilters()

    suspend fun getFilters() {
        RestaurantService.getTags().let {
            filters = Filters(it.tags, it.memberships)
        }
    }
}

data class SelectedFilters(var tags: MutableList<String> = mutableListOf(), var plan: Int? = null, var search: String? = null) {
    fun isAnyFilterSelected() = tags.isNotEmpty() || plan != null || search !==null
}
data class Filters(var tags: Map<String, List<String>>, var plans: List<Membership>)