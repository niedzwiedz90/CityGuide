package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;

/**
 * Created by Niedzwiecki on 11/8/2017.
 */

public class EndlessAdapter<T> extends BaseRecyclerAdapter<T> {

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public BaseItemView<T> createViewItem(Context context, int viewType) {
        return null;
    }
}
