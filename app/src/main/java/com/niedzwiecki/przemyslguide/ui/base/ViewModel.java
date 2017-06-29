package com.niedzwiecki.przemyslguide.ui.base;


import android.os.Bundle;

/**
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface ViewModel {

    void attachNavigator(Navigator navigator);

    void detachNavigator();

    void onActivityResult(Navigator navigator, int requestCode, int resultCode, Bundle data);

    void onDestroy();

    void onCreate();

    void saveInstanceState(Bundle bundle);

    void restoreInstanceState(Bundle bundle);


}
