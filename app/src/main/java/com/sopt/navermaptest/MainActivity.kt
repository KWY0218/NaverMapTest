package com.sopt.navermaptest

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var locationSource1: FusedLocationSource
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = binding.naverMapView
        locationSource1 = FusedLocationSource(this, 1000)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(p0: NaverMap) {
        naverMap = p0.apply {
            locationSource = locationSource1
            minZoom = 11.0
            maxZoom = 16.0
            uiSettings.apply {
                isCompassEnabled = false
                isScaleBarEnabled = false
                isLocationButtonEnabled = true
                isLogoClickEnabled = false
            }
        }

        val marker1 = Marker()
        val marker2 = Marker()
        val listener = Overlay.OnClickListener { overlay ->
            val marker = overlay as Marker

            if (marker.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                binding.detailText.text = marker.captionText
                binding.mainDetailCl.visibility = View.VISIBLE
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                binding.mainDetailCl.visibility = View.INVISIBLE
            }

            true
        }
        marker1.position = LatLng(36.7637, 127.285)
        marker2.position = LatLng(36.7683, 127.2838)
        marker1.map = naverMap
        marker2.map = naverMap
        marker1.captionText = "초원"
        marker2.captionText = "기좋뷔"
        marker1.icon = OverlayImage.fromResource(R.drawable.ic_place)
        marker2.icon = OverlayImage.fromResource(R.drawable.ic_place)
        marker1.iconTintColor = Color.BLUE
        marker1.onClickListener = listener
        marker2.onClickListener = listener
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource1.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource1.isActivated) { // 권한 거부됨
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