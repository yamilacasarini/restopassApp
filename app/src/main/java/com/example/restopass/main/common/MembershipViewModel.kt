package com.example.restopass.main.common

import androidx.lifecycle.ViewModel
import com.example.restopass.R
import com.example.restopass.domain.Restaurant
import com.example.restopass.service.RestopassApi
import kotlinx.coroutines.*
import timber.log.Timber

class MembershipViewModel : ViewModel() {
    var membershipsList: List<Membership> = listOf()

    private var viewModelJob = Job()
    // El main dispatcher usa el UI thread
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    fun getMemberships() {
        runBlocking {
            val getPropertiesDeferred = RestopassApi.retrofitService.getMemberships()
            var listResult = getPropertiesDeferred.await()
            membershipsList = listResult.memberships
        }
    }

    //val membershipsList: List<Membership> = listOf(
//        Membership(MembershipType.GOLD, "Membresía Gold",
//            "Disfruta de la mejor membresía con comidas tales como sushi, BBQ Ribs, Arizona Pasta, Filet Mignon with mushroom sauce y muchos más.",
//            R.drawable.sushi,
//            5000, restaurants),
//        Membership(MembershipType.PLATINIUM, "Membresía Platinium",
//            "Disfruta de la membresía platinium con platos tales como bife blablabla.",
//            R.drawable.steak, 3500, restaurants),
//        Membership(MembershipType.STANDARD, "Membresía Estándar",
//            "Disfruta de la membresía estándard con platos tales como bife blablabla.",
//            R.drawable.hamburger, 2000, restaurants)
    //)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    companion object {
        private val restaurants = listOf(
            Restaurant("1", "Pepito", "Restaurante Pepito"),
            Restaurant("2", "Juancito", "Restaurante Juancito")
        )
    }
}