package com.example.restopass.main.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.common.orElse
import com.example.restopass.domain.MembershipsViewModel
import com.example.restopass.domain.RestaurantsViewModel
import com.example.restopass.main.common.membership.MembershipAdapter
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_membership.membershipRecyclerView
import kotlinx.coroutines.*
import timber.log.Timber

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var membershipAdapter: MembershipAdapter
    private lateinit var membershipsViewModel: MembershipsViewModel

    private lateinit var restaurantsViewModel: RestaurantsViewModel

    private val fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
    private val permissions = arrayOf(fineLocation, coarseLocation)
    private val permissionCode = 1234
    private var locationGranted = false
    private lateinit var location: LatLng

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        membershipsViewModel = ViewModelProvider(requireActivity()).get(MembershipsViewModel::class.java)
        restaurantsViewModel = ViewModelProvider(requireActivity()).get(RestaurantsViewModel::class.java)

        membershipAdapter =
            MembershipAdapter()
        recyclerView = membershipRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = membershipAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        initializeLocation()
        loader.visibility = View.VISIBLE
        AppPreferences.user?.actualMembership?.let {
            getLocation()
        }.orElse {
           getMemberships()
        }

    }

    private fun getMemberships() {
        coroutineScope.launch {
            try {
                membershipsViewModel.get()

                membershipAdapter.memberships = membershipsViewModel.memberships!!
                membershipAdapter.notifyDataSetChanged()
                loader.visibility = View.GONE
                membershipRecyclerView.visibility = View.VISIBLE
            } catch (e: Exception) {
                if(isActive) {
                    Timber.e(e)
                    view?.findNavController()?.navigate(R.id.refreshErrorFragment)
                }
            }
        }
    }

    private fun getRestaurants(latLng: LatLng) {
        coroutineScope.launch {
            try {
                restaurantsViewModel.get(latLng)


                loader.visibility = View.GONE
            } catch (e: Exception) {
                if(isActive) {
                    Timber.e(e)
                    view?.findNavController()?.navigate(R.id.refreshErrorFragment)
                }
            }
        }
    }

    private fun initializeLocation() {
        locationGranted = getLocationPermissions()
        if (!locationGranted)
            requestLocationPermission()
        else
            getLocation()
    }

    private fun getLocation() {
        val fuseLoc = this.context?.let { LocationServices.getFusedLocationProviderClient(it) }
        if (locationGranted) {
            fuseLoc?.lastLocation?.addOnSuccessListener  {lastLocation : Location? ->
                lastLocation?.let {
                    location = LatLng(it.latitude, it.longitude)
                    getRestaurants(location)

                }
            }
        }
    }

    private fun getLocationPermissions() = permissions.all { perm -> this.context?.let { ContextCompat.checkSelfPermission(it, perm) } == PackageManager.PERMISSION_GRANTED }

    private fun requestLocationPermission() = this.activity?.let { ActivityCompat.requestPermissions(it, permissions, permissionCode) }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults.all { it ==  PackageManager.PERMISSION_GRANTED }) {
                locationGranted = true
                getLocation()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}
