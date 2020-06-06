package com.example.restopass.service

import com.example.restopass.common.error
import com.example.restopass.connection.RetrofitFactory
import com.example.restopass.domain.Restaurant
import com.example.restopass.domain.Tags
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import timber.log.Timber


object RestaurantService {
    private var api: RestopassApi = RetrofitFactory.createClient(BASE_URL, RestopassApi::class.java)

    interface RestopassApi {
        @GET("restaurants/{lat}/{lng}")
        fun getRestaurantForLocationAsync(@Path("lat") latitude: Double, @Path("lng") longitude: Double):
                Deferred<Response<List<Restaurant>>>

        @GET("restaurants/tags")
        fun getRestaurantTagsAsync(): Deferred<Response<Tags>>
    }

    suspend fun getRestaurants(latLng: LatLng): List<Restaurant> {
        val response = api.getRestaurantForLocationAsync(latLng.latitude, latLng.longitude).await()
        Timber.i("Executed GET to ${response.raw()}. Response code was ${response.code()}")
        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }

    suspend fun getTags(): Tags {
        val response = api.getRestaurantTagsAsync().await()
        Timber.i("Executed GET to ${response.raw()}. Response code was ${response.code()}")
        return when {
            response.isSuccessful -> response.body()!!
            else -> throw response.error()
        }
    }
}