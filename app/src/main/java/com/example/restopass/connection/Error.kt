package com.example.restopass.connection

data class ApiError(
    val code: Number,
    val httpStatusCode: Number,
    val message: String
)