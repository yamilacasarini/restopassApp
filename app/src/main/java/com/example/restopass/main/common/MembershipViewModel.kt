package com.example.restopass.main.common

import androidx.lifecycle.ViewModel
import com.example.restopass.R

class MembershipViewModel : ViewModel() {
    val membershipsList: List<Membership> = memberships

    companion object {
        private val memberships = listOf(
           Membership(MembershipType.GOLD, "Membresía Gold", "Disfruta de la mejor membresía con platos tales como sushi, blablabla", R.drawable.sushi),
            Membership(MembershipType.PLATINIUM, "Membresía Platinium", "Disfruta de la membresía platinium con platos tales como bife blablabla", R.drawable.steak),
            Membership(MembershipType.STANDARD, "Membresía Estándar", "Disfruta de la membresía estándard con platos tales como bife blablabla", R.drawable.hamburger)
        )

    }
}