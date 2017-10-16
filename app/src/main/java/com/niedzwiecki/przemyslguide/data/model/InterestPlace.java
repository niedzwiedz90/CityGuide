package com.niedzwiecki.przemyslguide.data.model;

import java.io.Serializable;

/**
 * Created by niedzwiedz on 20.08.17.
 */

public class InterestPlace implements Serializable {
    public int id;
    public String image;
    public String name;
    public String address;
    public double longLocation;
    public double latLocation;

    public InterestPlace(int id, String image, String name, String address, double longLocation, double latLocation) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.address = address;
        this.longLocation = longLocation;
        this.latLocation = latLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongLocation() {
        return longLocation;
    }

    public void setLongLocation(double longLocation) {
        this.longLocation = longLocation;
    }

    public double getLatLocation() {
        return latLocation;
    }

    public void setLatLocation(double latLocation) {
        this.latLocation = latLocation;
    }

}
