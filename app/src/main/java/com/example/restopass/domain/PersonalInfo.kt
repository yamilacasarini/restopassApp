package com.example.restopass.domain

data class PersonalInfo(
    val name: String,
    val lastName: String,
    val email: String,
    val secondaryEmails: List<String>?
)

data class PersonalInfoRequest(
    val name: String,
    val lastName: String,
    val secondaryEmails: List<String>?,
    val password: String?
)
