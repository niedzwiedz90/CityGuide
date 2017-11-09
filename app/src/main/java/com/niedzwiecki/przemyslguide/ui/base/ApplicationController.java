package com.niedzwiecki.przemyslguide.ui.base;

import android.app.Application;

import com.niedzwiecki.przemyslguide.data.DataManager;

/**
 * Created by niedzwiedz on 29.06.17.
 */

public class ApplicationController extends Application {

    private static ApplicationController instance;
    DataManager dataManager;
    private DataModule dataModule;

    public static synchronized ApplicationController getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    public DataModule getDataModule() {
        return dataModule;
    }

    public void init() {
        initDateModule();
        initDataManager();
        dataManager = getDataModule().provideDataManager();
    }

    public void initDateModule() {
        dataModule = new DataModule(this);
    }

    private void initDataManager() {
        DataManager.init();
    }
}
