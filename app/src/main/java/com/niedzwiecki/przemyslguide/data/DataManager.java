package com.niedzwiecki.przemyslguide.data;

import android.support.annotation.NonNull;

import com.niedzwiecki.przemyslguide.data.local.DatabaseHelper;
import com.niedzwiecki.przemyslguide.data.local.PreferencesHelper;
import com.niedzwiecki.przemyslguide.data.local.PreferencesKeys;
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest;
import com.niedzwiecki.przemyslguide.ui.base.ApplicationController;
import com.niedzwiecki.przemyslguide.ui.base.DataModule;
import com.niedzwiecki.przemyslguide.ui.base.GuideApi;
import com.niedzwiecki.przemyslguide.ui.base.ResourcesManager;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DataManager {

    private final GuideApi guideService;
    private final DatabaseHelper databaseHelper;
    private final PreferencesHelper preferencesHelper;
    private static DataManager dataManager;

    public ResourcesManager getResourcesManager() {
        return resourcesManager;
    }

    private final ResourcesManager resourcesManager;

    public DataManager(GuideApi guideService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper, ResourcesManager resourcesManager) {
        this.guideService = guideService;
        this.preferencesHelper = preferencesHelper;
        this.databaseHelper = databaseHelper;
        this.resourcesManager = resourcesManager;
    }

    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

  /*  public Observable<Ribot> syncRibots() {
        return guideService.getPlaces()
                .concatMap(new Func1<List<Ribot>, Observable<Ribot>>() {
                    @Override
                    public Observable<Ribot> call(List<Ribot> ribots) {
                        return databaseHelper.setRibots(ribots);
                    }
                });
    }*/

    public Observable<List<PlaceOfInterest>> getPlaces() {
        return guideService.getPlaces();
    }

    public boolean contains(PreferencesKeys key) {
        return preferencesHelper.contains(key);
    }

 /*   public Observable<SuppliesModel> login(String email, String password) {
        String encoding = HttpRequest.Base64.encode(email + ":" + password);
        final String format = String.format("Basic %s", encoding);
        return guideService.getSupplies(format)
                .doOnNext(new Action1<SuppliesModel>() {
                    @Override
                    public void call(SuppliesModel suppliesListModel) {
                        if (suppliesListModel != null) {
                            storeAuthenticationHeader(format);
                        }
                    }
                });
    }*/


    private <T> Observable<T> decorateObservable(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
//                .onErrorReturn();
    }
/*

    private
    @Nullable
    <T> T handleError(Throwable throwable) {
        if (NetworkUtil.isHttpStatusCode(throwable, 401)) {
            clearAllUserData();
            resourcesManager.startLauncher();
            return null;
        } else {
            throw guideService.parseException(throwable);
        }
    }
*/

    public void storeAuthenticationHeader(String loginHeader) {
        preferencesHelper.setAuthenticationHeader(PreferencesKeys.LOGION_HEADER, loginHeader);
    }

    public static void init() {
        DataModule dataModule = ApplicationController.getInstance().getDataModule();
        dataManager = new DataManager(dataModule.provideApi(),
                dataModule.providePreferencesManager(),
                dataModule.provideDatabaseHelper(),
                dataModule.provideResourcesManager());
    }

    @NonNull
    public static DataManager getInstance() {
        if (dataManager == null) {
            init();
        }

        return dataManager;
    }

}
