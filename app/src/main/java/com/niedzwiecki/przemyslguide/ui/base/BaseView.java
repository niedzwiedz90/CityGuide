package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;

public interface BaseView {

    Context getContext();

    int contentId();

    void afterViews();

}
