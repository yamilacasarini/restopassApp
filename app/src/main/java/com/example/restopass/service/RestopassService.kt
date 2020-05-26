package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.main.common.Membership
import com.example.restopass.main.common.MembershipResponse
import com.example.restopass.main.common.Memberships
import com.example.restopass.main.common.MembershipsResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import timber.log.Timber


object RestopassService{
    private const val BASE_URL = "https://restopass.herokuapp.com/"

    interface RestopassApi{
        @GET("/memberships")
        fun getMembershipsAsync():
                Deferred<Response<MembershipsResponse>>
    }

    private var api: RestopassApi

    init {
        api = RetrofitFactory.createClient(BASE_URL, RestopassApi::class.java)
    }

    suspend fun getMemberships(): Memberships {
       val response = api.getMembershipsAsync().await()

        Timber.i("Executed POST to ${response.raw()}. Response code was ${response.code()}")
        return when {
            response.isSuccessful -> response.body()!!.toClient()
            else -> throw response.error()
        }
    }

    private fun MembershipsResponse.toClient() : Memberships {
        val actualMembership = this.actualMembership.toClient()
        val memberships = this.memberships.map {
            it.toClient()
        }.toMutableList()
        return Memberships(actualMembership, memberships)
    }

    private fun MembershipResponse.toClient() : Membership {
        return membershipInfo!!.let {
            Membership(it.membershipId, it.name, it.description, it.img, it.visits, it.price, this.restaurants)
        }
    }

}