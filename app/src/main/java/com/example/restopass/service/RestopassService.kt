package com.example.restopass.service

import com.example.restopass.main.common.ResponseMembership
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


private const val BASE_URL = "http://demo5349060.mockable.io"

var okHttpClient = OkHttpClient.Builder()
    .connectTimeout(1, TimeUnit.MINUTES)
    .readTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

interface RestopassApiInterface{

    @GET("/memberships")
    fun getMembershipsAsync():
            Deferred<ResponseMembership>

}
object RestopassApi{
    val connector: RestopassApiInterface by lazy { retrofit.create(RestopassApiInterface::class.java) }
}