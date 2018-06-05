package com.niedzwiecki.przemyslguide;

import android.app.Application;
import android.content.Context;
import timber.log.Timber;

public class ApplicationController extends Application  {

    //Added googleServices.json

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
//            Fabric.with(this, new Crashlytics());
        }
    }

    public static ApplicationController get(Context context) {
        return (ApplicationController) context.getApplicationContext();
    }

}
