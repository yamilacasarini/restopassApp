package com.example.restopass.service

import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.login.domain.Login
import com.example.restopass.login.domain.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

object LoginService {
    private const val BASE_URL = "http://192.168.1.100:9290"

    interface LoginApi{
        @POST("/users/login")
        fun signIn(@Body login: Login):
                Deferred<Response<User>>
    }

    private var api: LoginApi

    init {
        api = RetrofitFactory.createClient(BASE_URL, LoginApi::class.java)
    }

    suspend fun signIn(login: Login) = api.signIn(login).await()
}