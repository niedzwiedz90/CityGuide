package com.niedzwiecki.przemyslguide.data.model;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.List;

/**
 * Created by niedzwiedz on 05.09.17.
 */

@SuppressLint("ParcelCreator")
public class PlacesResponse implements Serializable {

    public List<InterestPlace> interestPlaces;

}
