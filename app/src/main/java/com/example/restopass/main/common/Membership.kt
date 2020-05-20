package com.example.restopass.main.common

import com.example.restopass.domain.Restaurant

enum class MembershipType {
    GOLD,
    PLATINIUM,
    STANDARD
}

class Membership(
    val type: MembershipType,
    val title: String,
    val description: String,
    val image: Int,
    val price: Number,
    val restaurants: List<Restaurant>) {

    val restaurantsSize = restaurants.size.toString()

}