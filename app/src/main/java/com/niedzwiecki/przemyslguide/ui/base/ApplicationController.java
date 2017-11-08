package com.niedzwiecki.przemyslguide.ui.base;

import com.niedzwiecki.przemyslguide.data.DataManager;

/**
 * Created by niedzwiedz on 29.06.17.
 */

public class ApplicationController {

    private static ApplicationController instance;
    private DataManager dataManager;
    private DataModule dataModule;

    public static synchronized ApplicationController getInstance() {
        return instance;
    }

    public DataModule getDataModule() {
        return dataModule;
    }

    public void initSdk(ApplicationController context) {
        dataManager = getDataModule().provideDataManager();
    }
}
