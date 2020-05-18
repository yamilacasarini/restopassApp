package com.example.restopass.main.commons

import android.view.View

enum class MembershipType {
    GOLD,
    PLATINIUM,
    STANDARD
}

data class Membership(val type: MembershipType, val title: String, val description: String, val image: Int, var restaurantsVisibility: Int = View.GONE)