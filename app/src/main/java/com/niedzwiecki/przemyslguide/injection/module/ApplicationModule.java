package com.niedzwiecki.przemyslguide.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.niedzwiecki.przemyslguide.data.remote.GuideService;
import com.niedzwiecki.przemyslguide.injection.ApplicationContext;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    GuideService provideRibotsService() {
        return GuideService.Creator.newRibotsService();
    }

}
