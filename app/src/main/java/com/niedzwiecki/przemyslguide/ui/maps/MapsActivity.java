package com.niedzwiecki.przemyslguide.ui.maps;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.util.RequestPermissionHelper;

import timber.log.Timber;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient apiClient;
    private RequestPermissionHelper requestPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        setupApiClient();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupApiClient();

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private synchronized void setupApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        onConnectedGoogleApiClient();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Timber.e("Failed connection Google Api Client");
                    }
                }).addApi();
        apiClient.connect();
    }

    public void onConnectedGoogleApiClient() {
        if (requestPermissionHelper.isPermissionGranted(new String[]{Manifest.permission.ACCESS_FINE_LOCATION})) {
            Location lastLocation = new Location(LOCATION_SERVICE);
            updateLocation(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
    }

    private void updateLocation(double latitude, double longitude) {
        LatLng location = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
