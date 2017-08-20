package com.niedzwiecki.przemyslguide.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * Created by niedzwiedz on 20.08.17.
 */

@AutoValue
public abstract class InterestPlace implements Parcelable {

    public abstract String image();
    public abstract String name();
    public abstract String address();
    public abstract double longLocation();
    public abstract double latLocation();

}
