package com.niedzwiecki.przemyslguide.ui.views;

import android.view.View;

/**
 * Created by Niedzwiecki on 6/05/2018.
 */

public interface BaseView {

    void beforeViews();

    void afterViews();

    View getView();

}
