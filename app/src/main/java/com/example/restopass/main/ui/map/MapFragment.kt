package com.example.restopass.main.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.restopass.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment : Fragment(), OnMapReadyCallback{

    private lateinit var mapViewModel: MapViewModel
    private var mMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProviders.of(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_map, container, false)
        if (gServicesOk()) {
            (map as SupportMapFragment?)?.getMapAsync(this)
            Log.e("asd", "called map async")
        }
        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Toast.makeText(this.context, "Map", Toast.LENGTH_SHORT).show()
        Log.e("asd", "onMapReady")
        mMap = googleMap
        // Add a marker in Sydney, Australia, and move the camera.
        //val sydney = LatLng((-34).toDouble(), 151.0)
        //mMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun gServicesOk(): Boolean {
        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.context)
        if (available == ConnectionResult.SUCCESS) {
            Log.d("asad", "Google play services is ok")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d("asad", "error but resolvable")
        } else {
            Log.d("asad", "errorrrr")
        }
        Toast.makeText(this.context, "No map req available", Toast.LENGTH_SHORT).show()
        return false
    }


}
