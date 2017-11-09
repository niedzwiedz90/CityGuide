package com.niedzwiecki.przemyslguide.util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.niedzwiecki.przemyslguide.data.model.InterestPlace;
import com.niedzwiecki.przemyslguide.data.remote.GuideService;

/**
 * Created by niedzwiedz on 20.08.17.
 */

public class DataFactory {

    @NonNull
    private static Gson getGson() {
        return GuideService.Creator.provideGson().create();
    }

    public static InterestPlace getIntrestPlace(String json) {
        return getGson().fromJson(json, InterestPlace.class);
    }

}
