package com.niedzwiecki.przemyslguide.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.data.StringManager;
import com.niedzwiecki.przemyslguide.data.SyncService;
import com.niedzwiecki.przemyslguide.data.local.DatabaseHelper;
import com.niedzwiecki.przemyslguide.data.local.PreferencesHelper;
import com.niedzwiecki.przemyslguide.data.remote.GuideService;
import com.niedzwiecki.przemyslguide.injection.ApplicationContext;
import com.niedzwiecki.przemyslguide.injection.module.ApplicationModule;
import com.niedzwiecki.przemyslguide.util.RxEventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SyncService syncService);

    @ApplicationContext Context context();
    Application application();
    GuideService ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();
    StringManager stringManager();

}
