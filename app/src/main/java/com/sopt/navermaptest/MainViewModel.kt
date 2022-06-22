package com.sopt.navermaptest

import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    val test = mutableListOf(
        MapData(
            id = 0,
            latitude = 36.7637,
            longitude = 127.285,
            name = "초원",
            star = 4.0,
            tagList = mutableListOf("저탄수", "친구들이랑 먹기 좋은"),
            representativeMenu = "쟁반",
            isCame = false,
            isScrap = true,
            isColor = false
        ),
        MapData(
            id = 1,
            latitude = 36.7669,
            longitude = 127.2845,
            name = "홉스",
            star = 5.0,
            tagList = mutableListOf("저지방"),
            representativeMenu = "치킨",
            isCame = true,
            isScrap = false,
            isColor = false
        ),
        MapData(
            id = 2,
            latitude = 36.7662,
            longitude = 127.2854,
            name = "수신반점",
            star = 3.0,
            tagList = mutableListOf("고단백"),
            representativeMenu = "짬뽕",
            isCame = false,
            isScrap = false,
            isColor = false
        ),
        MapData(
            id = 3,
            latitude = 36.7639,
            longitude = 127.285,
            name = "티바",
            star = 2.0,
            tagList = mutableListOf("저탄수"),
            representativeMenu = "양념치킨",
            isCame = true,
            isScrap = true,
            isColor = false
        ),
        MapData(
            id = 4,
            latitude = 36.7682,
            longitude = 127.2838,
            name = "기좋뷔",
            star = 4.0,
            tagList = mutableListOf("저탄수"),
            representativeMenu = "백반",
            isCame = false,
            isScrap = false,
            isColor = false
        ),
        MapData(
            id = 5,
            latitude = 36.7689,
            longitude = 127.2828,
            name = "고릴라밥",
            star = 4.0,
            tagList = mutableListOf("저지방"),
            representativeMenu = "고추장삼겹살",
            isCame = true,
            isScrap = false,
            isColor = true
        ),
        MapData(
            id = 6,
            latitude = 36.7667,
            longitude = 127.2843,
            name = "화덕 갈매기",
            star = 4.0,
            tagList = mutableListOf("저지방"),
            representativeMenu = "고기",
            isCame = false,
            isScrap = true,
            isColor = true
        ),
        MapData(
            id = 7,
            latitude = 36.7676,
            longitude = 127.2834,
            name = "맘스터치",
            star = 2.0,
            tagList = mutableListOf("고단백"),
            representativeMenu = "햄버거",
            isCame = true,
            isScrap = false,
            isColor = true
        ),
        MapData(
            id = 8,
            latitude = 36.7674,
            longitude = 127.2833,
            name = "김밥천국",
            star = 3.0,
            tagList = mutableListOf("고단백"),
            representativeMenu = "김밥",
            isCame = false,
            isScrap = false,
            isColor = true
        ),
        MapData(
            id = 9,
            latitude = 36.7668,
            longitude = 127.2847,
            name = "마슬랜",
            star = 4.0,
            tagList = mutableListOf("고단백"),
            representativeMenu = "김피탕",
            isCame = true,
            isScrap = true,
            isColor = true
        ),
    )
    val testHashMap : Map<String,MapData> = test.associateBy { it.name }
}