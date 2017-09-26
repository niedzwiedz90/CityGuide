package com.niedzwiecki.przemyslguide.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by niedzwiedz on 20.08.17.
 */

/*@AutoValue
public abstract class InterestPlace implements Parcelable {*/
/*
    public abstract int id();
    public abstract String image();
    public abstract String name();
    public abstract String address();
    public abstract double longLocation();
    public abstract double latLocation();*/

    public class InterestPlace implements Serializable {

        public int id;
        public  String image;
        public  String name;
        public  String address;
        public  double longLocation;
        public  double latLocation;
}
