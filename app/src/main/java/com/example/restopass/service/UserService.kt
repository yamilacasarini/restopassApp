package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.ApiError
import com.example.restopass.connection.Api4xxException
import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.login.domain.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import timber.log.Timber
import java.io.IOException

object UserService {

    interface UserApi {
        @POST("/users/favorite/{restaurantId}")
        fun favRestaurant(@Path("restaurantId") restaurantId: String): Deferred<Response<Void>>

        @POST("/users/unfavorite/{restaurantId}")
        fun unFavRestaurant(@Path("restaurantId") restaurantId: String): Deferred<Response<Void>>

        @POST("/users/check/{userId}")
        fun checkCanAddToReservation(
            @Path("userId") userId: String
        ): Deferred<Response<User>>

        @PATCH("/users/topic/subscribe")
        fun subscribeToTopicAsync(): Deferred<Response<Void>>

        @PATCH("/users/topic/unsubscribe")
        fun unsubscribeToTopicAsync(): Deferred<Response<Void>>
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

    suspend fun checkCanAddToReservation(userId: String): User {
        try {
            val response = api.checkCanAddToReservation(userId).await()
            Timber.i("Executed POST. Response code was ${response.code()}")

            return when {
                response.isSuccessful -> response.body()!!
                else -> throw response.error()
            }
        } catch (e: IOException) {
            if (e.localizedMessage !== null) throw Api4xxException(ApiError(1,1,e.localizedMessage!!))
            else throw e
        }
    }

    suspend fun subscribeToTopic() {
        val response = api.subscribeToTopicAsync().await()
        Timber.i("Executed PATCH. Response code was ${response.code()}")

        if (!response.isSuccessful) throw response.error()
    }

    suspend fun unsubscribeToTopic() {
        val response = api.unsubscribeToTopicAsync().await()
        Timber.i("Executed PATCH. Response code was ${response.code()}")

        if (!response.isSuccessful) throw response.error()
    }
}