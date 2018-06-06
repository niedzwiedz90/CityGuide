package com.niedzwiecki.przemyslguide.ui.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import butterknife.BindView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.niedzwiecki.przemyslguide.R
import com.niedzwiecki.przemyslguide.data.model.MyItem
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest
import com.niedzwiecki.przemyslguide.ui.main.MainActivity
import com.niedzwiecki.przemyslguide.ui.placeDetails.PlaceDetailsActivity
import java.util.*

open class MapsActivity : FragmentActivity(),
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private var map: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null
    private var placesResponse: ArrayList<PlaceOfInterest>? = null
    private var place: PlaceOfInterest? = null
    private var clusterManager: ClusterManager<MyItem>? = null

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        fetchExtraData()
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setUpToolbar()

    }

    private fun moveCamera() {
        if (place != null) {
            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(place!!.lat.toDouble(),
                    place!!.lon.toDouble()), 9f))
        }
    }

    private fun setUpToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        if (place != null) toolbar.setTitle(place?.name) else toolbar.setTitle(R.string.all_places)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.accent))
    }

    private fun fetchExtraData() {
        if (intent.hasExtra(PLACES_LIST)) {
            placesResponse = intent.getParcelableArrayListExtra(PLACES_LIST)
        } else if (intent.hasExtra(MainActivity.INTEREST_PLACE_KEY)) {
            place = intent.extras!!.getParcelable(MainActivity.INTEREST_PLACE_KEY)
        }
    }

    private fun filterPlaces(type: String) {
        if (placesResponse != null) {
            val tempList = ArrayList<PlaceOfInterest>()
            for (interestPlace in placesResponse!!) {
                if (interestPlace != null && interestPlace.type == type) {
                    tempList.add(interestPlace)
                } else {
                    tempList.add(interestPlace)
                }
            }

            val intent = Intent(this@MapsActivity, MapsActivity::class.java)
            intent.putExtra(PLACES_LIST, tempList)
            startActivity(intent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        map?.uiSettings?.isZoomControlsEnabled = true

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
                map!!.isMyLocationEnabled = true
                setUpCluster()
                gotToPoint()
            }
        } else {
            buildGoogleApiClient()
            map!!.isMyLocationEnabled = true
        }
    }

    @Synchronized protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this
            )
        }
    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }

        gotToPoint()

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }
    }

    private fun gotToPoint() {
        if (place != null) {
            val latLng = LatLng(place!!.lat.toDouble(), place!!.lon.toDouble())
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title(String.format("lat: %s, long: %s", place!!.lat, place!!.lon))
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            mCurrLocationMarker = map!!.addMarker(markerOptions)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            map!!.animateCamera(cameraUpdate)
        }
    }

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)


            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        map!!.isMyLocationEnabled = true
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    fun setPlacesMarkers(placesMarkers: ArrayList<PlaceOfInterest>) {
        for (placesResponse in placesMarkers) {
            if (map != null && placesResponse != null) {
                val latLng = LatLng(placesResponse.lat.toDouble(), placesResponse.lon.toDouble())
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title(String.format("lat: %s, long: %s", placesResponse.lat, placesResponse.lon))
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                mCurrLocationMarker = map!!.addMarker(markerOptions)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12f)
                map!!.animateCamera(cameraUpdate)
            }
        }
    }

    private fun setUpCluster() {
        if (placesResponse != null) {
            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placesResponse!![0].lat.toDouble(),
                    placesResponse!![0].lon.toDouble()), 9f))
            clusterManager = ClusterManager(this, map)
            map!!.setOnCameraIdleListener(clusterManager)
            map!!.setOnMarkerClickListener(clusterManager)
            addItems(placesResponse!!)
            clusterManager!!.setOnClusterItemClickListener { myItem ->
                startActivity(PlaceDetailsActivity.getStartIntent(applicationContext,
                        myItem.interestPlace))
                false
            }
        }
    }

    private fun addItems(placesResponse: List<PlaceOfInterest>) {
        for (interestPlace in placesResponse) {
            if (map != null) {
                val offsetItem = MyItem(
                        interestPlace.lat,
                        interestPlace.lon,
                        interestPlace.name,
                        interestPlace.description,
                        interestPlace.image)
                clusterManager!!.addItem(offsetItem)
            }
        }
    }

    companion object {

        val ALL_PLACES_KEY = "AllPlacesKey"
        val PLACES_LIST = "PlacesList"

        fun getStartIntent(context: Context, interestPlace: PlaceOfInterest): Intent {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra(MainActivity.INTEREST_PLACE_KEY, interestPlace)
            return intent
        }

        val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
}
