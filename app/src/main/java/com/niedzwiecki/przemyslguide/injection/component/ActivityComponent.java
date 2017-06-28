package com.niedzwiecki.przemyslguide.injection.component;

import dagger.Subcomponent;
import com.niedzwiecki.przemyslguide.injection.PerActivity;
import com.niedzwiecki.przemyslguide.injection.module.ActivityModule;
import com.niedzwiecki.przemyslguide.ui.main.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}
