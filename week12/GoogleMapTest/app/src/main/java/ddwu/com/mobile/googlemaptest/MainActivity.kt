package ddwu.com.mobile.googlemaptest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import ddwu.com.mobile.googlemaptest.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityTag"

    val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var geocoder : Geocoder
    private lateinit var currentLoc : Location

    private lateinit var googleMap : GoogleMap
    var centerMarker : Marker? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())
        getLastLocation()   // 최종위치 확인

        mainBinding.btnPermit.setOnClickListener {
            checkPermissions()
            addMarker(LatLng(37.606320, 127.041808))
            drawLine()
        }

        mainBinding.btnLastLoc.setOnClickListener {
            getLastLocation()
        }

        mainBinding.btnLocStart.setOnClickListener {
            startLocUpdates()
        }

        mainBinding.btnLocStop.setOnClickListener {
            fusedLocationClient.removeLocationUpdates(locCallback)
        }


        mainBinding.btnLocTitle.setOnClickListener {
            geocoder.getFromLocation(37.601025, 127.04153, 5) { addresses ->
                CoroutineScope(Dispatchers.Main).launch {
                    showData("위도: ${currentLoc.latitude}, 경도: ${currentLoc.longitude}")
                    showData(addresses.get(0).getAddressLine(0).toString())
                }
            }
        }

        showData("Geocoder isEnabled: ${Geocoder.isPresent()}")

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)
      /* val mapFragment: SupportMapFragment = mapFragment.getMapAsync (mapReadyCallback)*/
    }


    /*GoogleMap 로딩이 완료될 경우 실행하는 Callback*/
    val mapReadyCallback = object: OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            googleMap.setOnMarkerClickListener {
                Toast.makeText(this@MainActivity, it.tag.toString(), Toast.LENGTH_SHORT).show()
                false
            }
            googleMap.setOnMapClickListener {
                Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show()
            }
            googleMap.setOnInfoWindowClickListener {
                Toast.makeText(this@MainActivity, it.title, Toast.LENGTH_SHORT).show()
            }
            googleMap.setOnMapClickListener{
                Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    /*마커 추가*/
    fun addMarker(targetLoc: LatLng) {  // LatLng(37.606320, 127.041808)
        val markerOptions: MarkerOptions = MarkerOptions()
        markerOptions.position(targetLoc)
            .title("마커 제목")
            .snippet("마커 말풍선")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

        centerMarker = googleMap.addMarker(markerOptions)
        centerMarker?.showInfoWindow()
        centerMarker?.tag = "database_id"
    }


    /*선 추가*/
    fun drawLine() {
        val polylineOptions = PolylineOptions()
            .color(Color.RED)
            .add(LatLng(37.604151, 127.042453))
            .add(LatLng(37.605347, 127.041207))
            .add(LatLng(37.606038, 127.041344))
            .add(LatLng(37.606220, 127.041674))
            .add(LatLng(37.606631, 127.041595))
            .add(LatLng(37.606823, 127.042380))
        val line = googleMap.addPolyline(polylineOptions)
    }


    /*위치 정보 수신 시 수행할 동작을 정의하는 Callback*/
    val locCallback : LocationCallback = object : LocationCallback() {
        @SuppressLint("NewApi")
        override fun onLocationResult(locResult: LocationResult) {
            currentLoc = locResult.locations.get(0)
            geocoder.getFromLocation(currentLoc.latitude, currentLoc.longitude, 5) { addresses ->
                CoroutineScope(Dispatchers.Main).launch {
                    showData("위도: ${currentLoc.latitude}, 경도: ${currentLoc.longitude}")
                    showData(addresses?.get(0)?.getAddressLine(0).toString())
                }
            }
            val targetLoc = LatLng(currentLoc.latitude, currentLoc.longitude)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 17F))
        }
    }

    /*API 33 이전 사용 방식*/
//            CoroutineScope(Dispatchers.Main).launch {
//                val addresses = geocoder.getFromLocation(currentLoc.latitude, currentLoc.longitude, 5)
//                showData("위도: ${currentLoc.latitude}, 경도: ${currentLoc.longitude}")
//                showData(addresses?.get(0)?.getAddressLine(0).toString())
//            }


    /*위치 정보 수신 설정*/
    val locRequest = LocationRequest.Builder(5000)
        .setMinUpdateIntervalMillis(3000)
        .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        .build()

    /*위치 정보 수신 시작*/
    @SuppressLint("MissingPermission")
    private fun startLocUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locRequest,     // LocationRequest 객체
            locCallback,    // LocationCallback 객체
            Looper.getMainLooper()  // System 메시지 수신 Looper
        )
    }



    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locCallback)
    }

    /*LBSTest 관련*/
    //    최종위치 확인
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                showData(location.toString())
                currentLoc = location
            } else {
                currentLoc = Location("기본 위치")      // Last Location 이 null 경우 기본으로 설정
                currentLoc.latitude = 37.606816
                currentLoc.longitude = 127.042383
            }
        }
        fusedLocationClient.lastLocation.addOnFailureListener { e: Exception ->
            Log.d(TAG, e.toString())
        }
    }


    fun callExternalMap() {
        val locLatLng   // 위도/경도 정보로 지도 요청 시
                = String.format("geo:%f,%f?z=%d", 37.606320, 127.041808, 17)
        val locName     // 위치명으로 지도 요청 시
                = "https://www.google.co.kr/maps/place/" + "Hawolgok-dong"
        val route       // 출발-도착 정보 요청 시
                = String.format("https://www.google.co.kr/maps?saddr=%f,%f&daddr=%f,%f",
            37.606320, 127.041808, 37.601925, 127.041530)
        val uri = Uri.parse(locLatLng)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


    private fun showData(data : String) {
        mainBinding.tvData.setText(mainBinding.tvData.text.toString() + "\n${data}")
    }


    fun checkPermissions () {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            showData("Permissions are already granted")  // textView에 출력
        } else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    /*registerForActivityResult 는 startActivityForResult() 대체*/
    val locationPermissionRequest
            = registerForActivityResult( ActivityResultContracts.RequestMultiplePermissions() ) {
            permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                showData("FINE_LOCATION is granted")
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                showData("COARSE_LOCATION is granted")
            }
            else -> {
                showData("Location permissions are required")
            }
        }
    }
}
