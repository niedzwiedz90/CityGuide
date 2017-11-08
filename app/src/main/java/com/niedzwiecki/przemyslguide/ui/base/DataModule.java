package com.niedzwiecki.przemyslguide.ui.base;

import android.support.annotation.NonNull;

import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest;
import com.niedzwiecki.przemyslguide.data.model.SuppliesModel;
import com.niedzwiecki.przemyslguide.data.remote.GuideService;

import java.util.List;

import rx.Observable;

/**
 * Created by Niedzwiecki on 11/8/2017.
 */

public class DataModule {
    @NonNull
    public DataManager provideDataManager() {
        return DataManager.getInstance();
    }

    public GuideService provideApi() {
        return new GuideService() {
            @Override
            public Observable<List<PlaceOfInterest>> getRibots() {
                return null;
            }

            @Override
            public Observable<SuppliesModel> getSupplies(String format) {
                return null;
            }
        }
    }
}
