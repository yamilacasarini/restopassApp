package com.example.restopass.common

import com.example.restopass.connection.ApiError
import com.example.restopass.connection.ApiException
import com.example.restopass.connection.ObjectMapperProvider.mapper
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber


internal inline fun Any?.orElse(f: ()-> Unit): Any?{
    if (this == null){
        f()
    }
    return this
}

internal inline fun < reified T: Any > Response<T>.error() : ApiException {
    Timber.e("Error occurred. Deserializing body")

    val apiError = mapper.readValue(this.errorBody()!!.string(), ApiError::class.java)

    Timber.e("Error connecting with service: ${apiError.code}")

    return ApiException(apiError)
}

fun Any.toJson(): String {
    return mapper.writeValueAsString(this)
}

internal inline fun <reified T> String.fromJson(): T{
    return mapper.readValue(this,T::class.java)
}

internal inline fun <reified T> ResponseBody.fromJson(): T{
    return mapper.readValue(this.toJson(),T::class.java)
}