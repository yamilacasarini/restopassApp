package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.main.common.MembershipResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import timber.log.Timber


object RestopassService{
    private const val BASE_URL = "https://restopass.herokuapp.com/"

    interface RestopassApi{
        @Headers("userId: prueba@prueba.com")
        @GET("/memberships")
        fun getMembershipsAsync():
                Deferred<Response<MembershipResponse>>
    }

    private var api: RestopassApi

    init {
        api = RetrofitFactory.createClient(BASE_URL, RestopassApi::class.java)
    }

    suspend fun getMemberships(): MembershipResponse {
       val response = api.getMembershipsAsync().await()

        Timber.i("Executed POST to ${response.raw()}. Response code was ${response.code()}")
        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }


}