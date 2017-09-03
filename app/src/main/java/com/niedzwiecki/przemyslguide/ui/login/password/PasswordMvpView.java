package com.niedzwiecki.przemyslguide.ui.login.password;

/**
 * Created by Dawid N on 7/6/2016.
 */
public interface PasswordMvpView {

    void showErrorPassword(String string);

    void hideNextButton();

    void showNextButton();

    void disablePasswordError();

    void goToMainActivity();
}
