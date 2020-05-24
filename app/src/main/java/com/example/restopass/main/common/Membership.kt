package com.example.restopass.main.common

import com.example.restopass.domain.Restaurant

data class Membership(
    val name: String,
    val description: String? = null,
    val img: String? = null,
    val price: Number? = null,
    var restaurants: List<Restaurant>? = listOf(),
    val isActual: Boolean = false,
    val isTitle: Boolean = false)

data class ResponseMembership(
    val actualMembership: Membership,
    val memberships: MutableList<Membership>
)