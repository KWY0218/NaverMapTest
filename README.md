# NaverMapTest
## Client Id 숨기기

### Local Properties

```kotlin
naver_key = "MY_CLIENT_ID"
```

### Build Gradle (:app)

```groovy
Properties properties = new Properties()
properties.load(project.rootProject.file('local.properties').newDataInputStream())
android {
  resValue("string","NAVER_KEY",properties.getProperty("naver_key"))
  manifestPlaceholders = [naver_key:"${properties.getProperty('naver_key')}"]
}
```

- `resValue` : 코드 단에서 사용 → `getString(R.string.*NAVER_KEY*)`
- `manifestPlaceholders` : Manifest에서 사용 → `android:value="${naver_key}"`

## Setting ([링크](https://navermaps.github.io/android-map-sdk/guide-ko/1.html))

### build gradle (:app)

```groovy
implementation 'com.naver.maps:map-sdk:3.15.0'

// location
implementation 'com.google.android.gms:play-services-location:20.0.0'

implementation 'com.android.support:appcompat-v7:28.0.0'
implementation('com.naver.maps:map-sdk:3.15.0'){
  exclude group: 'com.android.support'
}
```

### build gradle (:settings)

```groovy
repositories {
    google()
    mavenCentral()
    maven {
        url 'https://naver.jfrog.io/artifactory/maven/'
    }
}
```

### Manifest

```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>

<application
	...
	android:name=".App"
	...>

	<meta-data
		android:name="com.naver.maps.map.CLIENT_ID"
		android:value="${naver_key}" />
</application>
```

- 사용자 권한
- meta-data에 client id 등록

### local properties gradle

```kotlin
android.useAndroidX=true
android.enableJetifier=true
```

- Manifest 병합 오류 해결을 위해 등록

*과거 안드로이드에서 자주 사용된 support library를 사용하는 써드 파티 라이브러리를 우리가 개발하는 AndroidX 프로젝트와 호환되도록 돕는 툴이다.*

### Application 등록

```kotlin
class App : Application() {
    override fun onCreate() {
      super.onCreate()
      NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(getString(R.string.NAVER_KEY))
    }
}
```

## 지도 불러오기

```xml
<com.naver.maps.map.MapView
    android:id="@+id/naver_map_view"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    app:layout_constraintBottom_toTopOf="@+id/main_detail_cl"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent" />
```

- `xml` 내에 MapView 선언

```kotlin
class MainActivity : AppCompatActivity(), OnMapReadyCallback{
  private lateinit var mapView: MapView
  override fun onCreate(savedInstanceState: Bundle?) {
    ...
    mapView = binding.naverMapView
    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync(this)
    ...
  }

  override fun onMapReady(p0: NaverMap) {
    ...
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
```

- MapView 생명주기와 Activity 생명주기를 맞춘다
- `mapView.getMapAsync(this)` : 네이버 지도 객체를 비동기로 불러온다
- `override fun onMapReady(p0: NaverMap)` : 네이버 지도 초기 설정

## 권한 요청하기 ([링크](https://navermaps.github.io/android-map-sdk/guide-ko/4-2.html))

```kotlin
private lateinit var currentlocationSource: FusedLocationSource
override fun onCreate(savedInstanceState: Bundle?) {
	...
	currentlocationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
	...
}

override fun onMapReady(p0: NaverMap) {
	naverMap = p0.apply {
		...
		locationSource = currentlocationSource
		...
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
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }
  }
  super.onRequestPermissionsResult(requestCode, permissions, grantResults)
}

companion object {
  private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
}
```

## Naver Map 초기 설정 ([링크](https://navermaps.github.io/android-map-sdk/guide-ko/4-1.html))

```kotlin
private lateinit var naverMap: NaverMap
...
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
  }
```

- `locationSource` : 네이버 내장 위치 추적 기능 등록
- `minZoom` , `maxZoom` : 최대 줌, 아웃 설정
- `uiSettings.isCompassEnabled` : 나침반 on/off
- `uiSettings.isScaleBarEnabled` : 축척 바 on/off
- `uiSettings.isLocationButtonEnabled` : 현 위치 버튼 on/off
- `uiSettings.isLogoClickEnabled` : 네이버 로고 클릭 on/off
- `locationTrackingMode = LocationTrackingMode.Follow` : 위치 추적 모드 설정 (여기서 설정해야 카메라 첫 위치 자동으로 현제 위치로 이동함)
- `setOnMapClickListener` : map 클릭시 이벤트 처리

### Marker 코드단에서 등록 ([링크](https://navermaps.github.io/android-map-sdk/guide-ko/5-2.html))

```kotlin
override fun onMapReady(p0: NaverMap) {
  ...
  val listener = Overlay.OnClickListener { overlay ->
      val marker = overlay as Marker
      if (marker.infoWindow == null) {
          val marker = overlay as Marker
          if (marker.infoWindow == null) {
            ...
          }
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
```

- `onClickListener` : 마커 클릭시 이벤트 처리
- `position` : 마커 위치 설정, `map` 을 등록하기 전에 입력해야한다!!!
- `map` : 지도 객체 지정
- `captionText` : 마커 하단 이름 등록
- `icon` : 마커 이미지 커스텀 등록
- `iconTintColor` : 마커 색 변경

## 나머지 기능

- [네이버 Map API](https://navermaps.github.io/android-map-sdk/guide-ko/1.html)
