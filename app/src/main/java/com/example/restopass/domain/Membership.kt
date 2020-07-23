package com.example.restopass.domain

import androidx.lifecycle.ViewModel
import com.example.restopass.common.AppPreferences
import com.example.restopass.service.MembershipService

data class MembershipsResponse(
    val actualMembership: MembershipResponse?,
    val memberships: MutableList<MembershipResponse>
)

data class MembershipResponse(
    val membershipInfo: MembershipInfo,
    var restaurants: List<Restaurant>
)

data class DeleteMembershipResponse(
    val membershipFinalizeDate : String
)

data class MembershipInfo(
    val membershipId: Int,
    val name: String,
    val description: String,
    val img: String,
    val visits: Int,
    val price: Number
)

class Membership(
    val membershipId: Int? = null,
    val name: String,
    val description: String? = null,
    val img: String? = null,
    val visits: Int? = null,
    val price: Number? = null,
    var restaurants: List<Restaurant>? = listOf(),
    var isActual: Boolean = false,
    val isTitle: Boolean = false) {

    fun dishesAmount() = restaurants!!.flatMap { it.dishes }.size
    fun topNDishes(n: Int) = restaurants!!.flatMap { it.dishes }.sortedByDescending { it.stars }.take(n)
}

data class Memberships(
    var actualMembership: Membership?,
    var memberships: List<Membership>
)

class MembershipsViewModel : ViewModel() {
    var actualMembership: Membership? = null
    lateinit var memberships: MutableList<Membership>

    var selectedDetailsMembership: Membership? = null
    var selectedUpdateMembership: Membership? = null
    var wasEnrolled = false

    suspend fun get() {
        MembershipService.getMemberships().let {
            this.actualMembership = it.actualMembership
            this.memberships = it.memberships.toMutableList()
        }
    }

    suspend fun cancel() {
        MembershipService.cancelMembership().let {
            AppPreferences.user.apply {
                AppPreferences.user = this.copy(actualMembership = null, membershipFinalizeDate = it.membershipFinalizeDate)
            }
        }
    }

    suspend fun update(membership: Membership) {
        MembershipService.updateMembership(membership.membershipId!!).let {
           this.memberships = this.memberships.apply {
                actualMembership?.let { add(it) }
                remove(membership)
            }.sortedByDescending { it.membershipId }.toMutableList()

            this.actualMembership = membership

            AppPreferences.user.apply {
                AppPreferences.user = this.copy(actualMembership = membership.membershipId, visits = membership.visits!!)
            }

            wasEnrolled = true

        }
    }

    fun topTenDishes(): List<Dish> {
        val dishes = mutableListOf<Dish>()
        actualMembership?.topNDishes(3)?.toCollection(dishes)
        val otherMembershipsDishes = memberships.flatMap { it.topNDishes(10) }.sortedByDescending { it.stars }.take(7)
        return otherMembershipsDishes.toCollection(dishes).distinctBy { it.dishId  }
    }
}
