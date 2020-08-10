package com.example.restopass.domain

data class SecondaryEmail(
    val email: String,
    val confirmed: Boolean = true
)