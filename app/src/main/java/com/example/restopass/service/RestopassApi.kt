package com.example.restopass.service

import com.example.restopass.domain.MembershipsResponse
import com.example.restopass.domain.Restaurant
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface RestopassApi {
    @GET("/memberships")
    fun getMembershipsAsync():
            Deferred<Response<MembershipsResponse>>
    @GET("restaurants/{lat}/{lng}")
    fun getRestaurantForLocation(@Path("lat") latitude: Double, @Path("lng") longitude: Double):
            Deferred<Response<List<Restaurant>>>
}
