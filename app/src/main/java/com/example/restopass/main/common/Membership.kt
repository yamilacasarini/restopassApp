package com.example.restopass.main.common

import com.example.restopass.domain.Restaurant

enum class MembershipType {
    GOLD,
    PLATINIUM,
    STANDARD
}



data class Membership(
    val type: MembershipType,
    val title: String,
    val description: String,
    val image: String,
    val price: Number,
    val restaurants: List<Restaurant>) {

    val restaurantsSize = restaurants.size.toString()

}

data class ResponseMembership(
    val memberships: List<Membership>
)