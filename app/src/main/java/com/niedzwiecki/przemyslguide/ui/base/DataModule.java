package com.niedzwiecki.przemyslguide.ui.base;

import android.app.Application;
import android.support.annotation.NonNull;

import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.data.local.DatabaseHelper;
import com.niedzwiecki.przemyslguide.data.local.PreferencesHelper;

/**
 * Created by Niedzwiecki on 11/8/2017.
 */

public class DataModule {

    protected final Application application;

    public DataModule(Application application) {
        this.application = application;
    }

    @NonNull
    public DataManager provideDataManager() {
        return DataManager.getInstance();
    }

    @NonNull
    public DatabaseHelper provideDatabaseHelper() {
        return DatabaseHelper.getInstance();
    }

    @NonNull
    public ResourcesManager provideResourcesManager() {
        return ResourcesManager.getInstance(application);
    }

    @NonNull
    public GuideApi provideApi() {
        return new GuideApi(application);
    }

    @NonNull
    public PreferencesHelper providePreferencesManager() {
        return PreferencesHelper.getInstance(application);
    }

}
