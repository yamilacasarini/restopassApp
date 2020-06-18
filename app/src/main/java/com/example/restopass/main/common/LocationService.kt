package com.example.restopass.main.common

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.restopass.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

object LocationService {
    private val fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
    private val permissions = arrayOf(fineLocation, coarseLocation)
    val permissionCode = 1234
    private var locationGranted = false

    private lateinit var contextMain: Context
    private lateinit var activityMain: Activity
    private var fuseLoc: FusedLocationProviderClient? = null


    fun startLocationService(mainContext: Context, mainActivity: Activity) {
        activityMain = mainActivity
        contextMain = mainContext
        locationGranted = getLocationPermissions()
        if (!locationGranted)
            requestLocationPermission()
        else
            fuseLoc = getFuseLoc()
    }

    @SuppressLint("MissingPermission")
    fun addLocationListener(fn: (loc: Location?) -> Unit) {
            fuseLoc?.lastLocation?.addOnSuccessListener { lastLocation : Location? -> fn(lastLocation) }
    }

    fun isLocationGranted() = locationGranted

    private fun getFuseLoc() = LocationServices.getFusedLocationProviderClient(contextMain)

    private fun getLocationPermissions() =
        permissions.all { perm -> ContextCompat.checkSelfPermission(contextMain, perm)  == PackageManager.PERMISSION_GRANTED }

    private fun requestLocationPermission() = ActivityCompat.requestPermissions(activityMain, permissions, permissionCode)

    fun isLocationGranted(b: Boolean) {
        locationGranted = b
        fuseLoc = if (locationGranted) { getFuseLoc() } else { null }
    }
}
