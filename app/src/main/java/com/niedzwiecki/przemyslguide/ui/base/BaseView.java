package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;
import android.view.View;

public interface BaseView {

    Context getContext();

    int contentId();

    void afterViews();

    View getView();

}
