package com.example.restopass.connection.interceptor

import com.example.restopass.common.AppPreferences
import com.example.restopass.common.fromJson
import com.example.restopass.connection.ApiError
import com.example.restopass.login.domain.LoginResponse
import com.example.restopass.login.domain.LoginRestaurantResponse
import com.example.restopass.service.LoginService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response? {
        synchronized(this) {
            val originalRequest = chain.request()
            val request = originalRequest.withHeader("X-Auth-Token", AppPreferences.accessToken ?: "")

            val response = chain.proceed(request)

            return when {
                response.isSuccessful -> response
                else -> {
                    var rawJson: String
                    if (response.code() == 401) {
                        rawJson = response.body()!!.string()

                        val apiError: ApiError = rawJson.fromJson()

                        if (apiError.code == 40101) {
                            return resolveExpiredAccessToken(originalRequest, chain)
                        } else {
                            val apiError: ApiError = rawJson.fromJson()
                            throw IOException(apiError.message)
                        }
                    }
                    return response
                }
            }
        }
    }

}

private fun resolveExpiredAccessToken(
    originalRequest: Request,
    chain: Interceptor.Chain
): Response? {
    Timber.i("Access token has expired. Trying to get new refresh and access token")

    if (AppPreferences.restaurantUser != null) {
        val responseRestaurant = runBlocking {
            LoginService.refreshRestaurantToken(
                AppPreferences.accessToken!!,
                AppPreferences.refreshToken!!
            )
        }

        return when {
            responseRestaurant.isSuccessful -> {
                onRefreshSuccess(
                    chain,
                    originalRequest,
                    restaurantUserResponse = responseRestaurant.body()!!
                )
            }
            else -> {
                onRefreshError()
            }
        }

    } else {
        val responseUser = runBlocking {
            LoginService.refreshToken(AppPreferences.accessToken!!, AppPreferences.refreshToken!!)
        }

        return when {
            responseUser.isSuccessful -> {
                onRefreshSuccess(chain, originalRequest, userResponse = responseUser.body()!!)
            }
            else -> {
                onRefreshError()
            }
        }
    }

}

private fun onRefreshSuccess(
    chain: Interceptor.Chain,
    originalRequest: Request,
    restaurantUserResponse: LoginRestaurantResponse? = null,
    userResponse: LoginResponse? = null
): Response {
    Timber.i("Refresh token request success. Setting new refresh and access token")

    if (userResponse != null) {
        AppPreferences.apply {
            accessToken = userResponse.xAuthToken
            refreshToken = userResponse.xRefreshToken
            user = userResponse.user
        }
    }

    if (restaurantUserResponse != null) {
        AppPreferences.apply {
            accessToken = restaurantUserResponse.xAuthToken
            refreshToken = restaurantUserResponse.xRefreshToken
            restaurantUser = restaurantUserResponse.user
        }
    }

    Timber.i("Trying to make same old request with new access token")
    val newAuthenticationRequest =
        originalRequest.withHeader("X-Auth-Token", AppPreferences.accessToken!!)
    return chain.proceed(newAuthenticationRequest)
}

private fun onRefreshError(): Response {
    AppPreferences.logout()
    throw IOException()
}


private fun Request.withHeader(name: String, value: String): Request {
    return this.newBuilder()
        .header(name, value)
        .build()
}