package com.example.restopass.main.commons

enum class MembershipType {
    GOLD,
    PLATINIUM,
    STANDARD
}

data class MembershipItem(val type: MembershipType, val title: String, val description: String)