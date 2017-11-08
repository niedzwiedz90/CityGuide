package com.niedzwiecki.przemyslguide;

import android.app.Application;
import android.content.Context;

import com.niedzwiecki.przemyslguide.injection.component.ApplicationComponent;
//import com.niedzwiecki.przemyslguide.injection.component.DaggerApplicationComponent;
import com.niedzwiecki.przemyslguide.injection.module.ApplicationModule;

import timber.log.Timber;

public class BoilerplateApplication extends Application  {

    //Added googleServices.json

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
//            Fabric.with(this, new Crashlytics());
        }
    }

    public static BoilerplateApplication get(Context context) {
        return (BoilerplateApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
       /* if (mApplicationComponent == null) {
            mApplicationComponent = Dagg erApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }*/
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
