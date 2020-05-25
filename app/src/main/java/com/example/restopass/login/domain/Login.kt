package com.example.restopass.login.domain

data class Login(
    val email: String,
    val password: String
)

data class User(
    val userId: String,
    val xAuthToken: String,
    val xRefreshToken: String
)