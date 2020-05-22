package com.example.restopass.main.common

import com.example.restopass.domain.Restaurant

data class Membership(
    val title: String,
    val description: String,
    val image: String,
    val price: Number,
    val restaurants: List<Restaurant>,
    val isActual: Boolean = false)

data class ResponseMembership(
    val actual_plan: Membership,
    val memberships: MutableList<Membership>
)