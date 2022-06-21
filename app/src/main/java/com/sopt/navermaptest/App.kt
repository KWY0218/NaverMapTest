package com.sopt.navermaptest

import android.app.Application
import com.naver.maps.map.NaverMapSdk

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("t8r9jkf2zi")
    }
}