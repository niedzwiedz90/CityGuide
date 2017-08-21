package com.niedzwiecki.przemyslguide.ui.base;

import android.databinding.BaseObservable;
import android.os.Bundle;

/**
 * Created by niedzwiedz on 29.06.17.
 */

public class BaseViewModel<T extends Navigator> extends BaseObservable implements ViewModel {

    Navigator navigator;
    private T context;

    @Override
    public void attachNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void detachNavigator() {
        navigator = null;
    }

    @Override
    public void onActivityResult(Navigator navigator, int requestCode, int resultCode, Bundle data) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void saveInstanceState(Bundle bundle) {

    }

    @Override
    public void restoreInstanceState(Bundle bundle) {

    }

    @Override
    public void attachContext(Navigator context) {
        this.context = (T) context;
    }


    public boolean isNavigatorAttached() {
        return navigator != null;
    }

    public Navigator getNavigator() {
        checkNavigatorAttached();
        return navigator;
    }

    public void checkNavigatorAttached() {
        if (!isNavigatorAttached()) throw new MvpViewNotAttachedException();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, BaseActivity activity) {

    }

    public static class MvpViewNotAttachedException extends RuntimeException {

        public MvpViewNotAttachedException() {
            super("Please call attachNavigator(Context) before" +
                    " requesting data to the ViewModel");
        }
    }

}
