package com.niedzwiecki.przemyslguide.ui.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.widget.Toast
import butterknife.BindView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
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

class MapsActivity : FragmentActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var map: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null

    private var placesResponse: ArrayList<PlaceOfInterest>? = null
    private var place: PlaceOfInterest? = null

    private var clusterManager: ClusterManager<MyItem>? = null

    @BindView(R.id.navMapView)
    internal var navigationView: NavigationView? = null

    @BindView(R.id.drawerMapLayout)
    internal var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        if (intent.hasExtra(PLACES_LIST)) {
            placesResponse = intent.getParcelableArrayListExtra(PLACES_LIST)
        } else if (intent.hasExtra(MainActivity.INTEREST_PLACE_KEY)) {
            place = intent.extras!!.getParcelable(MainActivity.INTEREST_PLACE_KEY)
        }

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        init()
    }

    private fun init() {
        navigationView!!.setNavigationItemSelectedListener { item ->
            /*  if (item.isChecked()) {
                            item.setChecked(false);
                        } else {
                            item.setChecked(true);
                        }*/

            drawerLayout!!.closeDrawers()

            when (item.itemId) {
                R.id.navMap -> {
                    filterPlaces("all")
                    true
                }
                R.id.navLogout ->
                    //                                mainViewModel.logout();
                    true
                R.id.navMapWithHotels -> {
                    filterPlaces("hotel")
                    true
                }
                R.id.navMapWithCastles -> {
                    filterPlaces("castle")
                    true
                }
                R.id.navMapWithFort -> {
                    filterPlaces("station")
                    true
                }
                R.id.navMapWithBunker -> {
                    filterPlaces("bunker")
                    true
                }
                else -> true
            }
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

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
                map!!.isMyLocationEnabled = true
                setUpCluster()
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
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
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

        /*if (placesResponse != null) {
            //            setPlacesMarkers(placesResponse);
        } else */if (place != null) {
            val latLng = LatLng(place!!.lat.toDouble(), place!!.lon.toDouble())
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title(String.format("lat: %s, long: %s", place!!.lat, place!!.lon))
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            mCurrLocationMarker = map!!.addMarker(markerOptions)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            map!!.animateCamera(cameraUpdate)
        }

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }

    }

    fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        map!!.isMyLocationEnabled = true
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
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
