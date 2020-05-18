package com.example.restopass.main.commons

import androidx.lifecycle.ViewModel
import com.example.restopass.R

class MembershipViewModel : ViewModel() {
    val membershipsList: List<Membership> = memberships

    companion object {
        private val memberships = listOf(
           Membership(MembershipType.GOLD, "Membresía Gold", "Descripción gold", R.drawable.sushi),
            Membership(MembershipType.PLATINIUM, "Membresía Platinium", "Descripción platinium", R.drawable.steak)
        )

    }
}