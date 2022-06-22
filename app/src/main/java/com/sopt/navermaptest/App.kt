package com.sopt.navermaptest

import android.app.Application
import com.naver.maps.map.NaverMapSdk

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(getString(R.string.NAVER_KEY))
    }
}