package com.example.restopass.domain

enum class MembershipType {
    BASIC,
    GOLD,
    PLATINUM;

    fun greaterMemberships() = values().filter { it > this }
}

data class MembershipsResponse(
    val actualMembership: MembershipResponse?,
    val memberships: MutableList<MembershipResponse>
)

data class MembershipResponse(
    val membershipInfo: MembershipInfo? = null,
    var restaurants: List<Restaurant>? = listOf()
)

data class MembershipInfo(
    val membershipId: MembershipType,
    val name: String,
    val description: String? = null,
    val img: String? = null,
    val visits: Number? = null,
    val price: Number? = null
)

data class Memberships(
    val actualMembership: Membership?,
    val memberships: MutableList<Membership>
)

data class Membership(
    val membershipId: MembershipType? = null,
    val name: String,
    val description: String? = null,
    val img: String? = null,
    val visits: Number? = null,
    val price: Number? = null,
    var restaurants: List<Restaurant>? = listOf(),
    val isActual: Boolean = false,
    val isTitle: Boolean = false)



