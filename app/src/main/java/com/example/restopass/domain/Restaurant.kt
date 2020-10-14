package com.example.restopass.domain

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.restopass.login.domain.User
import com.example.restopass.service.RestaurantService
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class Restaurant(
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
    var comments: List<Comment> = listOf()
) {
    init {
        this.dishes.map { it.restaurantId = this.restaurantId }

        val firstDish = dishes[0]
        this.comments = listOf(
            Comment(User("holanda", "Yami", "PPepe", null, 1, null, null, null, null, false), 4.toFloat(), 4.5.toFloat(), firstDish, "Me pareció piola", LocalDateTime.now()),
            Comment(User("holanda", "Juanito", "PPepe", null, 1, null, null, null, null, false), 3.toFloat(), 2.1.toFloat(), firstDish, "Un restaurant muy elegante con un servicio impecable. Los mozos con muy buenos modales, aunque la comida algo fría y tardía", LocalDateTime.now()),
            Comment(User("holanda", "Tobi", "PPepe", null, 1, null, null, null, null, false), 3.toFloat(), 2.1.toFloat(), firstDish, "La comida muy buena. La atención excelente. Quisiera remarcar que me olvidé la billetera y el restaurant logró contactarme a través de mi mail que tenía gracias a RetoPass, por lo que logré recuperarla", LocalDateTime.now()))
    }
}

data class Comment(
    val user: User,
    val restaurantRating: Float,
    val dishRating: Float,
    val dish: Dish,
    val description: String,
    val date: LocalDateTime
)

data class TimeTable(val openingDays: List<String>, val pairHours: List<PairHour>?)

class Dish(val dishId: String,
           val name: String,
           val description: String,
           val baseMembership: Int,
           val baseMembershipName: String,
           val stars: Float,
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