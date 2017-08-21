package com.niedzwiecki.przemyslguide.injection.component;

import dagger.Subcomponent;

import com.google.android.gms.vision.barcode.Barcode;
import com.niedzwiecki.przemyslguide.injection.PerActivity;
import com.niedzwiecki.przemyslguide.injection.module.ActivityModule;
import com.niedzwiecki.przemyslguide.ui.login.email.EmailActivity;
import com.niedzwiecki.przemyslguide.ui.login.password.PasswordActivity;
import com.niedzwiecki.przemyslguide.ui.main.MainActivity;
import com.niedzwiecki.przemyslguide.ui.placeDetails.PlaceDetailsActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(PlaceDetailsActivity placeDetailsActivity);

    void inject(EmailActivity emailActivity);

    void inject(PasswordActivity passwordActivity);
}
