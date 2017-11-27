package com.niedzwiecki.przemyslguide.ui.base;

/**
 * Created by Niedzwiecki on 11/11/2017.
 */

public interface Footer<T> extends BaseItemView<T> {

    void setInfoText(CharSequence text);

    void showError();

    void showProgressBar();

    void showInfo();

    void hide();

}
