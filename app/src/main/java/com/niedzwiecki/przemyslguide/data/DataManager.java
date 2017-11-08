package com.niedzwiecki.przemyslguide.data;

import com.niedzwiecki.przemyslguide.data.local.DatabaseHelper;
import com.niedzwiecki.przemyslguide.data.local.PreferencesHelper;
import com.niedzwiecki.przemyslguide.data.local.PreferencesKeys;
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest;
import com.niedzwiecki.przemyslguide.data.model.SuppliesModel;
import com.niedzwiecki.przemyslguide.data.remote.GuideService;
import com.niedzwiecki.przemyslguide.ui.base.ApplicationController;
import com.niedzwiecki.przemyslguide.ui.base.DataModule;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.fabric.sdk.android.services.network.HttpRequest;
import rx.Observable;
import rx.functions.Action1;

@Singleton
public class DataManager {

    private final GuideService mGuideService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final StringManager stringManager;
    private static DataManager dataManager;

    @Inject
    public DataManager(GuideService guideService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper, StringManager stringManager) {
        mGuideService = guideService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
        this.stringManager = stringManager;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

  /*  public Observable<Ribot> syncRibots() {
        return mGuideService.getPlaces()
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
        return mGuideService.getPlaces();
    }
*/

    public Observable<List<PlaceOfInterest>> getPlaces() {
        return mGuideService.getRibots();
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
        return mGuideService.getSupplies(format)
                .doOnNext(new Action1<SuppliesModel>() {
                    @Override
                    public void call(SuppliesModel suppliesListModel) {
                        if (suppliesListModel != null) {
                            storeAuthenticationHeader(format);
                        }
                    }
                });
    }

    public void storeAuthenticationHeader(String loginHeader) {
        mPreferencesHelper.setAuthenticationHeader(PreferencesKeys.LOGION_HEADER, loginHeader);
    }

    public static void init() {
        DataModule dataModule = ApplicationController.getInstance().getDataModule();
        dataManager = new DataManager(dataModule.provideApi(),
                NotificationController.getInstance(),
                dataModule.provideResourcesManager(),
                dataModule.provideContactManager(),
                dataModule.providePreferencesManager(),
                dataModule.provideDatabaseHelper(),
                dataModule.provideDatabaseCacheController()
        );
    }

    public static DataManager getInstance() {
        if (dataManager == null) {
            init();
        }

        return dataManager;
    }
}
