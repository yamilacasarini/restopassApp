package com.example.restopass.main.common

enum class MembershipType {
    GOLD,
    PLATINIUM,
    STANDARD
}

data class Membership(val type: MembershipType, val title: String, val description: String, val image: Int)