package com.example.restopass.domain

import com.example.restopass.R

data class CreditCard(
    val id: String? = null,
    val type: String,
    val holderName: String,
    val lastFourDigits: String
) {
    fun image(): Int {
        return imageResourceByType[type] ?: R.drawable.unknown_tc_logo
    }

    companion object {
        val imageResourceByType = mapOf(
            "VISA" to R.drawable.visa_logo,
            "AMERICAN_EXPRESS" to R.drawable.amex_logo,
            "MASTER_CARD" to R.drawable.mastercard_logo,
            "DISCOVER" to R.drawable.discover_logo,
            "DINERS_CLUB_CARD" to R.drawable.discover_logo,
            "UNKNOWN" to R.drawable.unknown_tc_logo
        )
    }
}