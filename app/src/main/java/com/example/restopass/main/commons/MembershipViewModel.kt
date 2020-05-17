package com.example.restopass.main.commons

import androidx.lifecycle.ViewModel

class MembershipViewModel : ViewModel() {
    val settingsItems: List<MembershipItem> = memberships

    companion object {
        private val memberships = listOf(
           MembershipItem(MembershipType.GOLD, "Membresía Gold", "Descripción gold")
        )

    }
}