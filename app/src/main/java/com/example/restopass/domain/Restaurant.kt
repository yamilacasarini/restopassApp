package com.example.restopass.domain

import androidx.lifecycle.ViewModel
import com.example.restopass.service.RestaurantService
import com.google.android.gms.maps.model.LatLng

data class Restaurant(
    val restaurantId: String,
    val name: String,
    val img: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val tags: List<String>,
    val timeTable: List<TimeTable>,
    val dishes: List<Dish>,
    val stars: Double
)

data class TimeTable(val openingDays: List<String>, val pairHours: List<PairHour>?)

data class Dish(val name: String, val description: String, val topMembership: MembershipType, val stars: Double)

data class PairHour(val  openingHour: Int, val  openingMinute: Int, val  closingHour: Int, val  closingMinute: Int)

data class RestaurantsViewModel(
    var restaurants: List<Restaurant>? = null
) : ViewModel() {
    suspend fun get(latLng: LatLng) {
        RestaurantService.getRestaurants(latLng).let {
            this.restaurants = it
        }
    }
}