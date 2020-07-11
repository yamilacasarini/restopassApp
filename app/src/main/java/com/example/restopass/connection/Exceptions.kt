package com.example.restopass.connection

open class Api4xxException(error: ApiError?, message: String? = null) :
    RestoPassException(error, message)

open class ApiFatalException(error: ApiError?, message: String? = null) :
    RestoPassException(error, message)

open class RestoPassException(error: ApiError?, message: String? = null) : RuntimeException(error?.message ?: message)