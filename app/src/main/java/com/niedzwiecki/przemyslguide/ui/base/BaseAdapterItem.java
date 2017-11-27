package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;

/**
 * Created by Niedzwiecki on 11/8/2017.
 */

public interface BaseAdapterItem<T> {

    BaseItemView<T> createViewItem(Context context, int viewType);

}
