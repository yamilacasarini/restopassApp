package com.example.restopass.connection.interceptor

import com.example.restopass.common.toJson
import com.example.restopass.connection.ApiError
import okhttp3.*
import java.net.SocketTimeoutException

class TimeoutInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response? {
        synchronized(this) {
            val request = chain.request()

            try {
                return chain.proceed(request)
            } catch (e: SocketTimeoutException) {
                val builder = Response.Builder()
                builder
                    .code(408)
                    .message("Socket Timeout")
                    .body(
                        ResponseBody.create(
                        MediaType.get("application/json"),
                        ApiError(408, 408, "Ups! Tuvimos un problema. Inténtalo más tarde").toJson())
                    )
                    .request(request)
                    .protocol(Protocol.HTTP_2)

                return builder.build()
            }
        }
    }
}