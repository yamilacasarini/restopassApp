package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path
import timber.log.Timber

object UserService {
    private const val BASE_URL = "https://restopass.herokuapp.com"

    interface UserApi {
        @POST("/users/favorite/{restaurantId}")
        fun favRestaurant(@Path("restaurantId") restaurantId: String): Deferred<Response<Void>>

        @POST("/users/unfavorite/{restaurantId}")
        fun unFavRestaurant(@Path("restaurantId") restaurantId: String): Deferred<Response<Void>>
    }

    private var api: UserApi

    init {
        api = RetrofitFactory.createClient(BASE_URL, UserApi::class.java)
    }

    suspend fun favorite(restaurantId: String) {
        val response = api.favRestaurant(restaurantId).await()
        Timber.i("Executed POST. Response code was ${response.code()}")

        if (!response.isSuccessful) throw response.error()
    }

    suspend fun unfavorite(restaurantId: String) {
        val response = api.unFavRestaurant(restaurantId).await()
        Timber.i("Executed POST. Response code was ${response.code()}")

        if (!response.isSuccessful) throw response.error()
    }
}