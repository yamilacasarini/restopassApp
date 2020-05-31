package com.example.restopass.domain

data class Dish(
    val name: String,
    val description: String,
    val topMembership: MembershipType,
    val stars: Number
)