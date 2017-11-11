package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Niedzwiecki on 11/11/2017.
 */

public class FooterView<T> extends LoadingView implements Footer<T>, BaseItemView<T> {

    public FooterView(Context context) {
        super(context);
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(int position, Object o) {

    }

    @Override
    public void beforeViews() {

    }

    @Override
    public void afterViews() {

    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setInfoText(CharSequence text) {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void showInfo() {

    }

    @Override
    public void hide() {

    }
}