package com.sopt.navermaptest

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        currentlocationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

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
            locationTrackingMode = LocationTrackingMode.Follow
            setOnMapClickListener { _, _ ->
                binding.mainDetailCl.visibility = View.GONE
            }
        }

        val listener = Overlay.OnClickListener { overlay ->
            val marker = overlay as Marker
            if (marker.infoWindow == null) {
                val data = viewModel.testHashMap[marker.captionText]!!
                val starNum = "%.1f".format(data.star)
                val mainMenu = "????????????: ${data.representativeMenu}"

                binding.detailTagCg.removeAllViews()
                binding.detailName.text = data.name
                binding.detailStarTv.text = starNum
                binding.detailMainMenuTv.text = mainMenu
                binding.mainDetailCl.visibility = View.VISIBLE
                data.tagList.forEach {
                    val chip = Chip(this)
                    chip.text = it
                    binding.detailTagCg.addView(chip)
                }
                binding.detailBookmarkImg.setOnClickListener { it.isSelected = !it.isSelected }
            } else {
                // ?????? ?????? ????????? ?????? ?????? ???????????? ?????? ??????
                binding.mainDetailCl.visibility = View.GONE
            }
            true
        }

        viewModel.test.forEach { data ->
            Marker().apply {
                position = LatLng(data.latitude, data.longitude)
                map = naverMap
                captionText = data.name
                icon = OverlayImage.fromResource(R.drawable.ic_baseline_place_24)
                iconTintColor =
                    if (data.isColor) ContextCompat.getColor(
                        this@MainActivity,
                        R.color.orange
                    ) else Color.GREEN
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
            if (!currentlocationSource.isActivated) { // ?????? ?????????
                naverMap.locationTrackingMode = LocationTrackingMode.None
                return
            } else {
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}