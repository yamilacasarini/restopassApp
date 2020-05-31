package com.example.restopass.connection

open class ApiException(error: ApiError?, message: String? = null) :
    RuntimeException(error?.message ?: message)