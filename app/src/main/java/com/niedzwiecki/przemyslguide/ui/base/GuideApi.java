package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;

import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest;
import com.niedzwiecki.przemyslguide.data.remote.GuideService;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by Niedzwiecki on 11/8/2017.
 */

public class GuideApi {

    public Retrofit retrofit;
    private GuideService guideService;

    private Context context;

    public GuideApi(Context context) {
        this.context = context;
        retrofit = GuideService.Creator.newRibotsService();
        guideService = retrofit.create(GuideService.class);
    }

    public Observable<List<PlaceOfInterest>> getPlaces() {
        return guideService.getPlaces();
    }

}
