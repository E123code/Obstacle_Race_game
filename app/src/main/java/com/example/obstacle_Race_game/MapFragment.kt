package com.example.obstacle_Race_game

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.obstacle_Race_game.model.RecordManager
import com.example.obstacle_Race_game.utilities.LocationHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map

        map.uiSettings.isZoomControlsEnabled = true

        enableMyLocationLayer()

        markRecords()

        zoomToUserLocation()
    }



    private fun enableMyLocationLayer() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            googleMap?.isMyLocationEnabled = true
            googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        }
    }

    private fun zoomToUserLocation() {
        val helper = LocationHelper(requireContext())
        helper.fetchLocation { lat, lng ->
            if (lat != 0.0 && lng != 0.0) {
                val userLatLng = LatLng(lat, lng)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 13f))
            }
        }
    }

    private fun markRecords(){
        val  scores = RecordManager.getRecords()
        for(score in scores){
            if(score.lat != 0.0 && score.lon !=0.0){
                val  position = LatLng(score.lat, score.lon)
                googleMap?.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(score.playerName)
                        .snippet("Score: ${score.highScore}")


                )
            }
        }
    }

    fun zoom(lat: Double, lon: Double) {
        val place = LatLng(lat,lon)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(place, 15f))
    }

}