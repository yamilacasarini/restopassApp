package com.example.restopass.main.common

import androidx.lifecycle.ViewModel
import com.example.restopass.R
import com.example.restopass.domain.Restaurant

class MembershipViewModel : ViewModel() {

    val membershipsList: List<Membership> = listOf(
        Membership(MembershipType.GOLD, "Membresía Gold",
            "Disfruta de la mejor membresía con comidas tales como sushi, BBQ Ribs, Arizona Pasta, Filet Mignon with mushroom sauce y muchos más.",
            R.drawable.sushi,
            5000, restaurants),
        Membership(MembershipType.PLATINIUM, "Membresía Platinium",
            "Disfruta de la membresía platinium con platos tales como bife blablabla.",
            R.drawable.steak, 3500, restaurants),
        Membership(MembershipType.STANDARD, "Membresía Estándar",
            "Disfruta de la membresía estándard con platos tales como bife blablabla.",
            R.drawable.hamburger, 2000, restaurants)
    )



    companion object {
        private val restaurants = listOf(
            Restaurant("1", "Pepito", "Restaurante Pepito"),
            Restaurant("2", "Juancito", "Restaurante Juancito")
        )
    }
}