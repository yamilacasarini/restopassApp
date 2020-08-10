package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.common.md5
import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.login.domain.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import timber.log.Timber

object LoginService {
    private const val BASE_URL = "https://restopass.herokuapp.com/"

    interface LoginApi{
        @POST("/users/login")
        fun signInAsync(@Body login: Login):
                Deferred<Response<LoginResponse>>

        @POST("/users/restaurants/login")
        fun signRestaurantInAsync(@Body login: Login):
                Deferred<Response<LoginRestaurantResponse>>

        @POST("/users/login/google")
        fun googleSignInAsync(@Body login: GoogleLogin):
                Deferred<Response<LoginResponse>>

        @POST("/users")
        fun signUpAsync(@Body signUpViewModel: SignUpViewModel):
                Deferred<Response<LoginResponse>>

        @GET("/users/refresh")
        fun refreshTokenAsync(@Header("X-Auth-Token") accessToken: String, @Header("X-Refresh-Token") refreshToken: String):
                Deferred<Response<LoginResponse>>

        @GET("/users/restaurants/refresh")
        fun refreshRestaurantTokenAsync(@Header("X-Auth-Token") accessToken: String, @Header("X-Refresh-Token") refreshToken: String):
                Deferred<Response<LoginRestaurantResponse>>
    }

    private var api: LoginApi

    init {
        api = RetrofitFactory.createClient(BASE_URL, LoginApi::class.java, false)
    }

    suspend fun signIn(login: Login): LoginResponse {
        val response = api.signInAsync(login.copy(password = login.password.md5())).await()
        Timber.i("Executed POST to ${response.raw()}. Response code was ${response.code()}")

        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }

    suspend fun signRestaurantIn(login: Login): LoginRestaurantResponse {
        val response = api.signRestaurantInAsync(login.copy(password = login.password.md5())).await()
        Timber.i("Executed POST to ${response.raw()}. Response code was ${response.code()}")

        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }

    suspend fun googleSignIn(token: String): LoginResponse {
        val response = api.googleSignInAsync(GoogleLogin(token)).await()
        Timber.i("Executed POST to ${response.raw()}. Response code was ${response.code()}")

        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }

    suspend fun signUp(signUpViewModel: SignUpViewModel): LoginResponse {
        signUpViewModel.password = signUpViewModel.password.md5()

        val response = api.signUpAsync(signUpViewModel).await()
        Timber.i("Executed POST to ${response.raw()}. Response code was ${response.code()}")

        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }

    suspend fun refreshToken(accessToken: String, refreshToken: String): Response<LoginResponse> {
        val response = api.refreshTokenAsync(accessToken, refreshToken).await()
        Timber.i("Executed POST to ${response.raw()}. Response code was ${response.code()}")
        return response
    }

    suspend fun refreshRestaurantToken(accessToken: String, refreshToken: String): Response<LoginRestaurantResponse> {
        val response = api.refreshRestaurantTokenAsync(accessToken, refreshToken).await()
        Timber.i("Executed POST to ${response.raw()}. Response code was ${response.code()}")
        return response
    }
}