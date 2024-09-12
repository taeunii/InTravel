package com.example.intravel

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.intravel.client.SubClient
import com.example.intravel.data.Maps
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.intravel.databinding.ActivityMapsBinding
import com.example.intravel.databinding.CustomMapBinding
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mapItemForFunc: Maps

    companion object {
        const val TAG = "MapActivity"
        const val REQUEST_LOCATION_PERMISSION = 1
    }

    lateinit var binding: ActivityMapsBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentMarker: Marker? = null

    private val markersMap = mutableMapOf<Marker, Maps>() // 마커와 데이터 매핑
    private var isButtonVisible = false // 수정, 삭제 버튼 사라지거나 나타나게 함

    private lateinit var searchEdit: EditText
    private lateinit var searchButton: Button

    private var tId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Google Places API 초기화
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyDkoP6r8hnWyrMknlg-3myS0PM5ZSULwfo")
        }

        // PlacesClient 생성
        val placesClient = Places.createClient(this)

        // View 바인딩 초기화
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 검색창 버튼 초기화
        searchEdit = findViewById(R.id.searchEdit)
        searchButton = findViewById(R.id.searchButton)

        // MapView 초기화 및 생성
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        // FusedLocationProviderClient 초기화 (현재 위치 정보 제공)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapView.getMapAsync(this)

        searchButton.setOnClickListener {
            val searchQuery = searchEdit.text.toString()
            if (searchQuery.isNotEmpty()) {
//                searchLocationByAddress(searchQuery)
                searchPlaceByName(searchQuery)
            }else {
                Toast.makeText(this,"주소를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        // Intent로부터 tId를 추출
        tId = intent.getLongExtra("tId", 0)

        // 핀 위치 지정 후 + 버튼 클릭 시 이벤트 처리
        binding.btnPinAdd.setOnClickListener {
            val currentLatLng = googleMap.cameraPosition.target // 현재 지도 중심 위치

            // 다이얼로그 생성
            val addDialog = CustomMapBinding.inflate(layoutInflater)

            AlertDialog.Builder(this).run {
                setView(addDialog.root)
                setPositiveButton("확인", object :DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val edtPinName = addDialog.edtPinName.text.toString()
                        val latitude = currentLatLng.latitude.toString()
                        val longitude = currentLatLng.longitude.toString()

                        // 핀 정보 데이터베이스 저장
                        val mapItem = Maps(0, tId, latitude, longitude, edtPinName)

                        SubClient.retrofit.insertMap(tId, mapItem).enqueue(object :retrofit2.Callback<Maps> {
                            override fun onResponse(call: Call<Maps>, response: Response<Maps>) {
                                // 지도에 핀 추가
                                val latLng = LatLng(latitude.toDouble(), longitude.toDouble())
                                val markerOptions = MarkerOptions().position(latLng).title(edtPinName)
                                val marker = googleMap.addMarker(markerOptions)

                                markersMap[marker] = mapItem

                                // 핀이 추가된 위치로 카메라 이동
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                            }
                            override fun onFailure(call: Call<Maps>, t: Throwable) {
                            }
                        })  //insertMap
                    }
                })
                .setNegativeButton("취소", null)
                .show()
            }   //AlertDialog
        }   //btnPinAdd
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        checkLocationPermission()

        // 현재 줌 레벨을 추적하기 위한 변수
        var previousZoom = googleMap.cameraPosition.zoom

        // 카메라가 움직일 때 호출되는 리스너
        googleMap.setOnCameraMoveListener {
            val currentZoom = googleMap.cameraPosition.zoom

            if (isButtonVisible) {
                binding.btnPinRemove.visibility = View.GONE
                binding.btnPinUpdate.visibility = View.GONE
                isButtonVisible = false
            }

            // 줌 레벨이 변했을 때만 처리
            if (currentZoom != previousZoom) {
                previousZoom = currentZoom
                currentMarker?.let {
                    val markerPosition = it.position
                    // 마커 위치를 기준으로 카메라를 이동
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, currentZoom))
                }
            }
        }

        // 카메라 이동이 끝났을 때 호출되는 리스너
        googleMap.setOnCameraIdleListener {
            val centerLatLng = googleMap.cameraPosition.target
            Log.d(TAG, "Camera Idle - Center: $centerLatLng")

            // 마커를 화면 중앙에 고정하지 않고, 지도 중심에 새로운 마커 추가
            currentMarker?.remove()
            currentMarker = setupMarker(LatLngEntity(centerLatLng.latitude, centerLatLng.longitude))
            currentMarker?.showInfoWindow()
        }

        // 모든 마커 불러오기
        SubClient.retrofit.findAllMap(tId).enqueue(object :retrofit2.Callback<List<Maps>> {
            override fun onResponse(call: Call<List<Maps>>, response: Response<List<Maps>>) {
                response.body()?.let { markers ->
                    if (this@MapsActivity::googleMap.isInitialized) {
                        // LatLngBounds.Builder 초기화
                        val builder = LatLngBounds.Builder()
                        Log.d("allData", "${response.body()}")

                        // 지도에 모든 마커 추가
                        for (marker in markers) {
                            val latLng = LatLng(marker.latitude.toDouble(), marker.longitude.toDouble())
                            val markerOptions = MarkerOptions().position(latLng).title(marker.pinName)
                            val googleMapMarker = googleMap.addMarker(markerOptions)

                            markersMap[googleMapMarker] = marker

                            // LatLngBounds에 마커 좌표 추가
                            builder.include(latLng)
                        }

                        // LatLngBounds 빌드
                        val bounds = builder.build()
                        // 지도에서 마커들이 모두 보이도록 카메라 조정
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100) // 100은 경계에 여유를 주기 위한 값
                        googleMap.moveCamera(cameraUpdate)
                    }
                }
            }
            override fun onFailure(call: Call<List<Maps>>, t: Throwable) {
            }
        })  //findAllMap

        // 저장된 핀 선택 시 핀 상부에 저장된 정보 노출
        googleMap.setOnMarkerClickListener { marker ->
            val mapItem = markersMap[marker]

            mapItemForFunc = mapItem!!

            marker.title = mapItem!!.pinName
            marker.snippet = getAddressFromLatLng(marker.position)
            marker.showInfoWindow()

            if (!isButtonVisible) {
                binding.btnPinRemove.visibility = View.VISIBLE
                binding.btnPinUpdate.visibility = View.VISIBLE
                isButtonVisible = true
            }

            true
        }   //setOnMarkerClickListener


        // 수정 버튼 클릭 시 다이얼로그 창 열림
        binding.btnPinUpdate.setOnClickListener {
            val updateDialog = CustomMapBinding.inflate(layoutInflater)
            updateDialog.edtPinName.setText(mapItemForFunc.pinName) // 기존 데이터 표시

            AlertDialog.Builder(this).run {
                setView(updateDialog.root)
                setPositiveButton("수정", object :DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val updatePinName = updateDialog.edtPinName.text.toString()
                        val latitude = mapItemForFunc.latitude
                        val longitude = mapItemForFunc.longitude

                        // 핀 정보 데이터베이스 저장
                        val updateMapItem = Maps(
                            mapItemForFunc.mapId,
                            mapItemForFunc.travId,
                            latitude,
                            longitude,
                            updatePinName
                        )

                        SubClient.retrofit.updateMap(mapItemForFunc.mapId, updateMapItem).enqueue(object : retrofit2.Callback<Maps> {
                            override fun onResponse(call: Call<Maps>, response: Response<Maps>) {
                                val updatedMarker = markersMap.entries.find { it.value.mapId == mapItemForFunc.mapId }?.key
                                updatedMarker?.apply {
                                    title = updatePinName
                                    showInfoWindow()
                                }
                            }
                            override fun onFailure(call: Call<Maps>, t: Throwable) {
                            }
                        })
                    }
                })
                setNegativeButton("취소", null)
                show()
            }   //AlertDialog
        }   //btnPinUpdate

        // 삭제 버튼 클릭 시 다이얼로그 창 열림
        binding.btnPinRemove.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle("삭제하시겠습니까?")
                setPositiveButton("삭제", object :DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        SubClient.retrofit.deleteByIdMap(mapItemForFunc.mapId).enqueue(object :retrofit2.Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                // 화면에서 핀 삭제
                                val markerToRemove = markersMap.entries.find { it.value.mapId == mapItemForFunc.mapId }?.key
                                markerToRemove?.remove()

                                // 맵에서 마커와 관련된 데이터 제거
                                markersMap.entries.removeIf { it.value.mapId == mapItemForFunc.mapId }
                            }
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                            }
                        })
                    }
                })
                setNegativeButton("취소", null)
                show()
            }   //AlertDialog
        }   //btnPinRemove
    }

    private fun searchLocationByAddress(address: String) {
        val geocoder = Geocoder(this,Locale.getDefault())
        try {
            val address = geocoder.getFromLocationName(address, 1)
            if (address != null && address.isNotEmpty()) {
                val location = address[0]
                val latLng = LatLng(location.latitude, location.longitude)
                Log.d("주소 검색 결과: $latLng", toString())

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                currentMarker?.remove()
                currentMarker = setupMarker(LatLngEntity(latLng.latitude,latLng.longitude))
                currentMarker?.showInfoWindow()
            }else {
                Toast.makeText(this, "해당 주소를 찾을 수 없습니다.", Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception) {
            Log.e(TAG, "주소 검색 중 오류 발생: $address", e)
            Toast.makeText(this, "주소 검색 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show()
        }
    }

    private fun searchPlaceByName(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        val placesClient = Places.createClient(this)

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            val prediction = response.autocompletePredictions.firstOrNull()
            if (prediction != null) {
                val placeId = prediction.placeId

                // 장소 ID를 사용하여 세부 정보를 가져옵니다.
                val placeRequest = FetchPlaceRequest.builder(placeId, listOf(Place.Field.LAT_LNG)).build()
                placesClient.fetchPlace(placeRequest).addOnSuccessListener { placeResponse ->
                    val latLng = placeResponse.place.latLng
                    if (latLng != null) {
                        // 검색된 장소로 카메라 이동
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                        // 마커 추가
                        currentMarker?.remove()
                        currentMarker = setupMarker(LatLngEntity(latLng.latitude, latLng.longitude))
                        currentMarker?.showInfoWindow()
                    }
                }
            } else {
                Toast.makeText(this, "해당 장소를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "장소 검색 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                Log.d(TAG, "Current Location: $currentLatLng")

                // 카메라를 현재 위치로 이동
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                // 마커 추가
                currentMarker?.remove()
                currentMarker = setupMarker(LatLngEntity(it.latitude, it.longitude))
                currentMarker?.showInfoWindow()
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

            val address = getAddressFromLatLng(positionLatLng)
            snippet(address)
            Log.d("address", address)
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
        super.onDestroy()
        mapView.onDestroy()
    }

    data class LatLngEntity(
        var latitude: Double?,
        var longitude: Double?
    )
}





//        // 저장된 핀 선택 시 - 버튼 보이고, 수정 다이얼로그 창 열림
//        googleMap.setOnMarkerClickListener { marker ->
//            val mapItem = markersMap[marker]
//            Log.d("MapDebug", "Marker clicked: ${marker.title}, Data: $mapItem")
//
//            val updateDialog = CustomMapBinding.inflate(layoutInflater)
//            updateDialog.edtPinName.setText(mapItem!!.pinName) // 기존 데이터 표시
//
//            AlertDialog.Builder(this).run {
//                setTitle("저장된 위치")
//                setView(updateDialog.root)
//                setPositiveButton("수정", object :DialogInterface.OnClickListener {
//                    override fun onClick(p0: DialogInterface?, p1: Int) {
//                        val updatePinName = updateDialog.edtPinName.text.toString()
//                        val latitude = marker.position.latitude.toString()
//                        val longitude = marker.position.longitude.toString()
//
//                        // 핀 정보 데이터베이스 저장
//                        val updateMapItem = Maps(mapItem.mapId,
//                            mapItem.travId,
//                            latitude,
//                            longitude,
//                            updatePinName)
//
//                        SubClient.retrofit.updateMap(mapItem.mapId, updateMapItem).enqueue(object :retrofit2.Callback<Maps> {
//                            override fun onResponse(call: Call<Maps>, response: Response<Maps>) {
//                                marker.title = updatePinName
//                                marker.showInfoWindow()
//                            }
//                            override fun onFailure(call: Call<Maps>, t: Throwable) {
//                            }
//                        })
//                    }
//                })
//                setNegativeButton("취소", null)
//                show()
//            }
//            true
//        }