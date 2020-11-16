package com.example.restopass.domain

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.restopass.login.domain.User
import com.example.restopass.service.RestaurantService
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class
Restaurant(
    val restaurantId: String,
    val name: String,
    val img: String,
    val address: String,
    val location: Location,
    val tags: List<String>,
    val timeTable: List<TimeTable>,
    val dishes: List<Dish>,
    val hoursToCancel : Int,
    val stars: Float,
    val comments: List<Comment>?
) {
    init {
        this.dishes.map { it.restaurantId = this.restaurantId }
    }
}

data class Comment(
    val user: User,
    val restaurantStars: Float,
    val dishStars: Float,
    val dish: Dish,
    val description: String?,
    val date: String
)

data class TimeTable(val openingDays: List<String>, val pairHours: List<PairHour>?)

class Dish(val dishId: String,
           val name: String,
           val description: String,
           val baseMembership: Int,
           val baseMembershipName: String,
           val stars: Float,
           val tags : List<String>?,
           val img: String,
           var restaurantId: String? = null) {
    fun isIncluded(membershipId: Int) = this.baseMembership <= membershipId
}

data class PairHour(val  openingHour: Int, val  openingMinute: Int, val  closingHour: Int, val  closingMinute: Int)

data class Tags(val memberships: List<Membership>, val tags: Map<String, List<String>>) //tags are title to tags

data class Location(val x: Double, val y: Double)

class RestaurantViewModel : ViewModel() {
    lateinit var restaurant: Restaurant

    suspend fun get(id: String) {
        RestaurantService.getRestaurant(id).let {
            this.restaurant = it
        }
    }
}