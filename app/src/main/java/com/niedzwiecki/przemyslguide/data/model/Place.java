package com.niedzwiecki.przemyslguide.data.model;

import java.io.Serializable;

/**
 * Created by Niedzwiecki on 11/1/2017.
 */

public class Place implements Serializable {

    public long id;
    public String url;
    public float lat;
    public float lon;
    public String type;
    public String name;
    public String descritpion;
    public String image;
    public String email;
    public String telephone;

    public Place(int id, String image, String name, double lng, double lat) {
    }
}
