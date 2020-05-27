package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.login.domain.Login
import com.example.restopass.login.domain.LoginResponse
import com.example.restopass.login.domain.SignUpViewModel
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
        fun signIn(@Body login: Login):
                Deferred<Response<LoginResponse>>

        @POST("/users")
        fun signUp(@Body signUpViewModel: SignUpViewModel):
                Deferred<Response<LoginResponse>>

        @GET("/users/refresh")
        fun refreshToken(@Header("X-Auth-Token") accessToken: String, @Header("X-Refresh-Token") refreshToken: String):
                Deferred<Response<LoginResponse>>
    }

    private var api: LoginApi

    init {
        api = RetrofitFactory.createClient(BASE_URL, LoginApi::class.java, false)
    }

    suspend fun signIn(login: Login): LoginResponse {
        val response = api.signIn(login).await()
        Timber.i("Executed POST to ${response.raw()}. Response code was ${response.code()}")

        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }

    suspend fun signUp(signUpViewModel: SignUpViewModel): LoginResponse {
        val response = api.signUp(signUpViewModel).await()
        Timber.i("Executed POST to ${response.raw()}. Response code was ${response.code()}")

        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }

    suspend fun refreshToken(accessToken: String, refreshToken: String): Response<LoginResponse> {
        val response = api.refreshToken(accessToken, refreshToken).await()
        Timber.i("Executed POST to ${response.raw()}. Response code was ${response.code()}")
        return response
    }
}