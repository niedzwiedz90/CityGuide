package com.niedzwiecki.przemyslguide.ui.base;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the ViewModel type that wants to be attached with.
 */
public interface Presenter<V extends ViewModel> {

    void attachView(V mvpView);

    void detachView();
}
