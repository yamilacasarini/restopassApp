package com.example.restopass.domain

data class PersonalInfo(
    val name: String,
    val lastName: String,
    val email: String,
    val secondaryEmails: MutableList<String>? = mutableListOf()
)


data class PersonalInfoRequest(
    val name: String,
    val lastName: String,
    val secondaryEmails: MutableList<String>?,
    val password: String?
)
