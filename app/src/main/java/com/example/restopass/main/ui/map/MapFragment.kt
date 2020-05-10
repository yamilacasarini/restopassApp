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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =  this.activity?.supportFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment?
        Log.e("asd", "map exists: " + (mapFragment != null).toString())
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Toast.makeText(this.context, "Map", Toast.LENGTH_SHORT).show()
        Log.e("asd", "onMapReady")
        mMap = googleMap
        // Add a marker in Sydney, Australia, and move the camera.
        val sydney = LatLng((-34).toDouble(), 151.0)
        mMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }



}
