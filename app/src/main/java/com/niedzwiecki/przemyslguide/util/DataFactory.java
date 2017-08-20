package com.niedzwiecki.przemyslguide.util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.niedzwiecki.przemyslguide.data.model.InterestPlace;
import com.niedzwiecki.przemyslguide.data.remote.RibotsService;

/**
 * Created by niedzwiedz on 20.08.17.
 */

public class DataFactory {

    @NonNull
    private static Gson getGson() {
        return RibotsService.Creator.provideGson().create();
    }

    public static InterestPlace getIntrestPlace(String json) {
        return getGson().fromJson(json, InterestPlace.class);
    }

}
