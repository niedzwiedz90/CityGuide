package com.niedzwiecki.przemyslguide.data.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Niedzwiecki on 10/9/2017.
 */

public class MyItem implements ClusterItem {

    private final LatLng mPosition;
    private String imagePlace;

    public int id;
    public String image;

    public String namePlace;
    public String addressPlace;

    PlaceOfInterest interestPlace;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(float lat, float lng, String name, String address, String image) {
        mPosition = new LatLng(lat, lng);
        namePlace = name;
        addressPlace = address;
        imagePlace = image;
        interestPlace = new PlaceOfInterest(0, image, name, lng, lat);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return namePlace;
    }

    @Override
    public String getSnippet() {
        return addressPlace;
    }

    public PlaceOfInterest getInterestPlace() {
        return interestPlace;
    }
}
