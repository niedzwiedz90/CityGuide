package com.niedzwiecki.przemyslguide.data;

import com.niedzwiecki.przemyslguide.data.local.DatabaseHelper;
import com.niedzwiecki.przemyslguide.data.local.PreferencesHelper;
import com.niedzwiecki.przemyslguide.data.local.PreferencesKeys;
import com.niedzwiecki.przemyslguide.data.model.InterestPlace;
import com.niedzwiecki.przemyslguide.data.model.Place;
import com.niedzwiecki.przemyslguide.data.model.Places;
import com.niedzwiecki.przemyslguide.data.model.PlacesResponse;
import com.niedzwiecki.przemyslguide.data.model.Ribot;
import com.niedzwiecki.przemyslguide.data.model.SuppliesModel;
import com.niedzwiecki.przemyslguide.data.remote.RibotsService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.fabric.sdk.android.services.network.HttpRequest;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

@Singleton
public class DataManager {

    private final RibotsService mRibotsService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final StringManager stringManager;

    @Inject
    public DataManager(RibotsService ribotsService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper, StringManager stringManager) {
        mRibotsService = ribotsService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
        this.stringManager = stringManager;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

  /*  public Observable<Ribot> syncRibots() {
        return mRibotsService.getPlaces()
                .concatMap(new Func1<List<Ribot>, Observable<Ribot>>() {
                    @Override
                    public Observable<Ribot> call(List<Ribot> ribots) {
                        return mDatabaseHelper.setRibots(ribots);
                    }
                });
    }*/

/*
    public Observable<List<Ribot>> getPlaces() {
        return mDatabaseHelper.getPlaces().distinct();
    }
*/
/*

    public Observable<PlacesResponse> getPlaces() {
        return mRibotsService.getPlaces();
    }
*/

    public Observable<List<Place>> getPlaces() {
        return mRibotsService.getRibots();
    }


    public String getString(int stringId) {
        return stringManager.getStringFromStringResource(stringId);
    }

    public boolean contains(PreferencesKeys key) {
        return mPreferencesHelper.contains(key);
    }

    public Observable<SuppliesModel> login(String email, String password) {
        String encoding = HttpRequest.Base64.encode(email + ":" + password);
        final String format = String.format("Basic %s", encoding);
        return mRibotsService.getSupplies(format)
                .doOnNext(new Action1<SuppliesModel>() {
                    @Override
                    public void call(SuppliesModel suppliesListModel) {
                        if(suppliesListModel != null) {
                            storeAuthenticationHeader(format);
                        }
                    }
                });
    }

    public void storeAuthenticationHeader(String loginHeader) {
        mPreferencesHelper.setAuthenticationHeader(PreferencesKeys.LOGION_HEADER, loginHeader);
    }

}
