package com.mobile.week14.findrestproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mobile.week14.findrestproject.data.Item
import com.mobile.week14.findrestproject.data.RestaurantRoot
import com.mobile.week14.findrestproject.databinding.ActivitySearchBinding
import com.mobile.week14.findrestproject.network.RegionApiService
import com.mobile.week14.findrestproject.ui.RestaurantAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(){
    private val TAG = "SearchActivityTag"

    val searchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    val adapter by lazy {
        RestaurantAdapter(this)
    }

    private lateinit var googleMap : GoogleMap
    private lateinit var  apiLocation : LatLng
    private var items: List<Item>? = null
    lateinit var apiCallback : Callback<RestaurantRoot>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(searchBinding.root)

        searchBinding.rvRestaurants.adapter = adapter
        searchBinding.rvRestaurants.layoutManager = LinearLayoutManager(this)

        val mapFragment:SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.naver_api_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RegionApiService::class.java)
        apiCallback = object : Callback<RestaurantRoot> {
            override fun onResponse(call: Call<RestaurantRoot>, response: Response<RestaurantRoot>) {
                if (response.isSuccessful) {
                    val root: RestaurantRoot? = response.body()
                    items = root?.items
                    runOnUiThread {
                        adapter.restaurants = items
                        adapter.notifyDataSetChanged()
                    }

                    if (!items.isNullOrEmpty()) {
                        var firstItem: Item = items!![0]
                        apiLocation = LatLng(firstItem.mapy/1e7, firstItem.mapx/1e7)
                        runOnUiThread {
                            getMap()
                        }
                    }
                } else {
                    Log.d(TAG, "Unsuccessful Response")
                }
            }

            override fun onFailure(call: Call<RestaurantRoot>, t: Throwable) {
                Log.d(TAG, "OpenAPI Call Failure ${t.message}")
            }
        }
        searchBinding.btnSearch.isEnabled = false

        searchBinding.btnSearch.setOnClickListener {
            val keyword = searchBinding.etKeyword.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val apiCall: Call<RestaurantRoot> = service.getRegionsByKeyword(
                        resources.getString(R.string.client_id),
                        resources.getString(R.string.client_secret),
                        keyword,
                        4
                    )
                    withContext(Dispatchers.Main) {
                        apiCall.enqueue(apiCallback)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error during network request: ${e.message}")
                }
            }
        }

        adapter.setMyItemClickListener(object : RestaurantAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int)  {
                val restaurant = adapter.restaurants?.get(position)
                var intent = Intent(applicationContext, RestaurantDetailActivity::class.java)
                intent.apply {
                    this.putExtra("title",restaurant?.title)
                    this.putExtra("category",restaurant?.category)
                    this.putExtra("address",restaurant?.address)
                }
                startActivity(intent)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        googleMap?.clear()
    }

    val mapReadyCallback = object: OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map
            searchBinding.btnSearch.isEnabled = true
        }
    }

    fun getMap() {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(apiLocation, 17F))
        for (item in items!!) {
            val location: LatLng = LatLng(item.mapy / 1e7, item.mapx / 1e7)
            addMarker(location, item)
        }
    }

    fun addMarker(targetLoc: LatLng, item: Item) {
        val markerOptions: MarkerOptions = MarkerOptions()
        markerOptions.position(targetLoc)
            .title(item.title)
            .snippet(item.category)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

        val centerMarker = googleMap.addMarker(markerOptions)
        centerMarker?.showInfoWindow()

        googleMap.setOnMarkerClickListener { marker ->
            Toast.makeText(this, marker.title.toString(), Toast.LENGTH_SHORT).show()
            false
        }
        googleMap.setOnInfoWindowClickListener { marker ->
            Toast.makeText(this, marker.title, Toast.LENGTH_SHORT).show()
            false
        }
    }

}
