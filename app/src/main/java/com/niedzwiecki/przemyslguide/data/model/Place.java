package com.niedzwiecki.przemyslguide.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Niedzwiecki on 11/1/2017.
 */

public class Place implements Parcelable {

    public long id;
    public float lat;
    public float lon;
    public String type;
    public String name;
    public String description;
    public String image;
    public String email;
    public String telephone;

    public Place(int id, String image, String name, double lng, double lat) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeFloat(lat);
        parcel.writeFloat(lon);
        parcel.writeString(type);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeString(email);
        parcel.writeString(telephone);
    }

    private Place(Parcel in) {
        id = in.readLong();
        lat = in.readFloat();
        lon = in.readFloat();
        type = in.readString();
        name = in.readString();
        description = in.readString();
        image = in.readString();
        email = in.readString();
        telephone = in.readString();
    }

    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];

        }
    };

    // all get , set method
}
