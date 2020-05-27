package com.example.restopass.domain

import com.example.restopass.main.common.MembershipType

data class Dish(
    val name: String,
    val description: String,
    val topMembership: MembershipType,
    val stars: Number
)