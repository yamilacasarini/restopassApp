package com.example.restopass.domain

data class CreditCard(
    val id: String,
    val owner: String,
    val number: String,
    val type: String
)