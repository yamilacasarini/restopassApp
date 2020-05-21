package com.example.restopass.service

import com.example.restopass.main.common.Membership
import com.example.restopass.main.common.ResponseMembership
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://demo5349060.mockable.io"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface RestopassApiInterface{

    @GET("/memberships")
    fun getMemberships():
            Deferred<ResponseMembership>

}
object RestopassApi{
    val retrofitService: RestopassApiInterface by lazy { retrofit.create(RestopassApiInterface::class.java) }
}