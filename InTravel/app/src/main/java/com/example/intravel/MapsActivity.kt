package com.example.intravel

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.intravel.databinding.ActivityMapsBinding
import com.google.android.gms.maps.MapView
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val TAG = "MapActivity"
        const val REQUEST_LOCATION_PERMISSION = 1
    }

    lateinit var binding: ActivityMapsBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View 바인딩 초기화
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // MapView 초기화 및 생성
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        // FusedLocationProviderClient 초기화 (현재 위치 정보 제공)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        // 위치 권한 확인 및 현재 위치 업데이트
        checkLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                Log.d(TAG, "Current Location: $currentLatLng")

                // 기존 마커 제거
                currentMarker?.remove()

                // 새 위치에 마커 설정
                currentMarker = setupMarker(LatLngEntity(it.latitude, it.longitude))
                currentMarker?.showInfoWindow()

                // 위치 정보를 바탕으로 주소 가져오기
                val address = getAddressFromLatLng(currentLatLng)
                Toast.makeText(this, "현재 주소: $address", Toast.LENGTH_LONG).show()

                // 카메라를 현재 위치로 이동
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            } ?: run {
                Log.e(TAG, "Location is null")
            }
        }.addOnFailureListener {
            Log.e(TAG, "Failed to get location", it)
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            updateCurrentLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateCurrentLocation()
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupMarker(locationLatLngEntity: LatLngEntity): Marker? {
        val positionLatLng = LatLng(locationLatLngEntity.latitude!!, locationLatLngEntity.longitude!!)
        val markerOption = MarkerOptions().apply {
            position(positionLatLng)
            title("현재 위치")
            snippet("현재 위치에 대한 정보")
        }
        return googleMap.addMarker(markerOption)
    }

    private fun getAddressFromLatLng(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                addresses[0].getAddressLine(0)
            } else {
                "주소를 찾을 수 없습니다."
            }
        } catch (e: Exception) {
            "주소를 가져오는 중 오류가 발생했습니다."
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    data class LatLngEntity(
        var latitude: Double?,
        var longitude: Double?
    )
}
