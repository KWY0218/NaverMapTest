package com.sopt.navermaptest

data class MapData(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val star: Double,
    val tagList: List<String>,
    val representativeMenu: String,
    val isCame : Boolean,
    val isScrap: Boolean,
    val isColor: Boolean
)
