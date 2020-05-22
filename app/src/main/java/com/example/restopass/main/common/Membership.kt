package com.example.restopass.main.common

import com.example.restopass.domain.Restaurant

data class Membership(
    val title: String,
    val description: String? = null,
    val image: String? = null,
    val price: Number? = null,
    val restaurants: List<Restaurant> = listOf(),
    val isActual: Boolean = false,
    val isTitle: Boolean = false)

data class ResponseMembership(
    val actual_plan: Membership,
    val memberships: MutableList<Membership>
)