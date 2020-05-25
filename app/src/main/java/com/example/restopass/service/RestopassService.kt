package com.example.restopass.service

import com.example.restopass.main.common.ResponseMembership
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit


object RestopassService{
    private const val BASE_URL = "https://restopass.herokuapp.com/"

    interface RestopassApi{
        @Headers("userId: prueba@prueba.com")
        @GET("/memberships")
        fun getMembershipsAsync():
                Deferred<Response<ResponseMembership>>
    }

    private var api: RestopassApi

    init {
        api = RetrofitFactory.createClient(BASE_URL, RestopassApi::class.java)
    }

    suspend fun getMemberships() = api.getMembershipsAsync().await()
}