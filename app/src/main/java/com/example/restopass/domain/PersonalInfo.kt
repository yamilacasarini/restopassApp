package com.example.restopass.domain

data class PersonalInfo(
    val name: String,
    val lastName: String,
    val email: String,
    val secondaryEmails: MutableList<SecondaryEmail> = mutableListOf(),
    val password: String? = null
)


data class PersonalInfoRequest(
    val name: String,
    val lastName: String,
    val toConfirmEmails: MutableList<String>? = mutableListOf(),
    val password: String?
)

data class PersonalInfoResponse(
    val name: String,
    val lastName: String,
    val email: String,
    val secondaryEmails: List<String> = listOf(),
    val toConfirmEmails: List<String> = listOf(),
    val password: String? = null
)
