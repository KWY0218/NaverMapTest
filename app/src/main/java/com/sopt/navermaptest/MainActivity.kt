package com.sopt.navermaptest

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.sopt.navermaptest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentlocationSource: FusedLocationSource
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = binding.naverMapView
        currentlocationSource = FusedLocationSource(this, 1000)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onMapReady(p0: NaverMap) {
        naverMap = p0.apply {
            locationSource = currentlocationSource
            minZoom = 11.0
            maxZoom = 16.0
            uiSettings.apply {
                isCompassEnabled = false
                isScaleBarEnabled = false
                isLocationButtonEnabled = true
                isLogoClickEnabled = false
            }
            setOnMapClickListener { _, _ ->
                binding.mainDetailCl.visibility = View.GONE
            }
        }

        val listener = Overlay.OnClickListener { overlay ->
            val marker = overlay as Marker
            if (marker.infoWindow == null) {
                val data = viewModel.testHashMap[marker.captionText]!!
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                binding.detailTagCg.removeAllViews()
                binding.detailName.text = data.name
                binding.detailStarTv.text = "%.1f".format(data.star)
                binding.detailMainMenuTv.text = "대표음식: ${data.representativeMenu}"
                binding.mainDetailCl.visibility = View.VISIBLE
                data.tagList.forEach {
                    val chip = Chip(this)
                    chip.text = it
                    binding.detailTagCg.addView(chip)
                }
                binding.detailBookmarkImg.setOnClickListener { it.isSelected = !it.isSelected }
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                binding.mainDetailCl.visibility = View.GONE
            }
            true
        }

        viewModel.test.forEach { data ->
            Marker().apply {
                position = LatLng(data.latitude, data.longitude)
                map = naverMap
                captionText = data.name
                icon = OverlayImage.fromResource(R.drawable.ic_place)
                iconTintColor =
                    if (data.isColor) Color.parseColor("#F19F37") else Color.GREEN

                onClickListener = listener
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (currentlocationSource.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        ) {
            if (!currentlocationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
                return
            } else {
                naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}